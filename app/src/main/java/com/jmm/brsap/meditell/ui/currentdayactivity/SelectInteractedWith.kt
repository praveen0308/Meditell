package com.jmm.brsap.meditell.ui.currentdayactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.CurrentAreaPersonAdapter
import com.jmm.brsap.meditell.adapters.CurrentDateAreaVisitAdapter
import com.jmm.brsap.meditell.databinding.FragmentSelectInteractedWithBinding
import com.jmm.brsap.meditell.model.Doctor
import com.jmm.brsap.meditell.model.Pharmacy
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.util.UserTypes
import com.jmm.brsap.meditell.viewmodel.CurrentDayActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectInteractedWith :
    BaseFragment<FragmentSelectInteractedWithBinding>(FragmentSelectInteractedWithBinding::inflate),
    CurrentAreaPersonAdapter.CurrentAreaPersonInterface {
    private val viewModel by activityViewModels<CurrentDayActivityViewModel>()

    private lateinit var currentAreaPersonAdapter: CurrentAreaPersonAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvPersons()
        viewModel.getCurrentAreaDoctorsAndPharmacy()

    }

    override fun onResume() {
        super.onResume()

    }

    override fun subscribeObservers() {
        viewModel.areaPersons.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        currentAreaPersonAdapter.setDoctorList(it)
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

    private fun setupRvPersons() {
        currentAreaPersonAdapter = CurrentAreaPersonAdapter(this)
        binding.rvData.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                layoutManager.orientation
            )
            addItemDecoration(dividerItemDecoration)

            this.layoutManager = layoutManager
            adapter = currentAreaPersonAdapter
        }
    }

    override fun onRowClick(item: Any) {
        if (item is Doctor){
            viewModel.selectedInteractedWith = "doctor"
            viewModel.selectedInteractiveInfo = "Doctor : ${item.name.toString()}"
            viewModel.selectedInteractedId = item.doctorId!!
        }
        else if (item is Pharmacy){
            viewModel.selectedInteractedWith = "pharmacy"
            viewModel.selectedInteractiveInfo = "Pharmacy : ${item.address.toString()}"
            viewModel.selectedInteractedId = item.pharmacyId!!
        }

//        findNavController().navigate(SelectInteractedWithDirections.actionSelectInteractedWith2ToSubmitInteraction2())
    }
}