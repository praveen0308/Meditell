package com.jmm.brsap.meditell.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentAddNewAreaBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.util.BaseBottomSheetDialogFragment
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.ManageAreaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewArea : BaseBottomSheetDialogFragment<FragmentAddNewAreaBinding>(FragmentAddNewAreaBinding::inflate) {

    private val viewModel by viewModels<ManageAreaViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSubmit.setOnClickListener {
                val areaName =etAreaName.text.toString().trim()
                val areaAddress =etAreaAddress.text.toString().trim()
                viewModel.addNewArea(Area(
                    name = areaName,
                    addressInfo = areaAddress,
                    active = true
                ))
            }
        }

    }
    override fun subscribeObservers() {
        viewModel.isAdded.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        showToast("Added successfully !!!")
                        dismiss()
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
}