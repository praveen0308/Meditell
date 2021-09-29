package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.ScheduleAdapter
import com.jmm.brsap.meditell.databinding.FragmentConfirmScheduleBinding
import com.jmm.brsap.meditell.databinding.FragmentCreateScheduleBinding
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.viewmodel.AddScheduleViewModel

class ConfirmSchedule : BaseFragment<FragmentConfirmScheduleBinding>(FragmentConfirmScheduleBinding::inflate),
    ScheduleAdapter.ScheduleInterface {
    private val viewModel by activityViewModels<AddScheduleViewModel>()
    private lateinit var scheduleAdapter:ScheduleAdapter

    override fun onResume() {
        super.onResume()
        viewModel.activeStep.postValue(2)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvSchedules()

    }

    override fun subscribeObservers() {

    }
    private fun setupRvSchedules(){
        scheduleAdapter = ScheduleAdapter(this)
        binding.rvSchedule.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(context,
                layoutManager.orientation)
            addItemDecoration(dividerItemDecoration)

            this.layoutManager = layoutManager
            adapter =scheduleAdapter
        }

        scheduleAdapter.setScheduleList(viewModel.scheduleList)
    }
}