package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.ChooseAreaAdapter
import com.jmm.brsap.meditell.databinding.FragmentChooseAreaBinding
import com.jmm.brsap.meditell.ui.AddNewArea
import com.jmm.brsap.meditell.util.BaseBottomSheetDialogFragment
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.AddScheduleViewModel
import com.jmm.brsap.meditell.viewmodel.ManageAreaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseArea : BaseBottomSheetDialogFragment<FragmentChooseAreaBinding>(FragmentChooseAreaBinding::inflate),
    ChooseAreaAdapter.ChooseAreaInterface {

    private val viewModel by viewModels<ManageAreaViewModel>()
    private lateinit var chooseAreaAdapter: ChooseAreaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvAreas()
        viewModel.getAreas()

        binding.btnAddNewArea.setOnClickListener {
            val sheet = AddNewArea()
            sheet.show(parentFragmentManager,sheet.tag)
        }
    }
    override fun subscribeObservers() {
        viewModel.areas.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                       chooseAreaAdapter.setAreaList(it)
                    }

                    displayLoading(false)
                }
                Status.LOADING -> {
                    displayLoading(true)
                }
                Status.ERROR -> {
                    displayLoading(false)
                    _result.message?.let {
                        displayError(it)
                    }
                }
            }
        })
    }

    private fun setupRvAreas(){
        chooseAreaAdapter = ChooseAreaAdapter(this)
        binding.rvAreas.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(context,
                layoutManager.orientation)
            addItemDecoration(dividerItemDecoration)

            this.layoutManager = layoutManager
            adapter = chooseAreaAdapter
        }
    }
}