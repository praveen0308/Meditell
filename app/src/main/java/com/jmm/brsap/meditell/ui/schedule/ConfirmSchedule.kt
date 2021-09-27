package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentConfirmScheduleBinding
import com.jmm.brsap.meditell.databinding.FragmentCreateScheduleBinding
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.viewmodel.AddScheduleViewModel

class ConfirmSchedule : BaseFragment<FragmentConfirmScheduleBinding>(FragmentConfirmScheduleBinding::inflate) {
    private val viewModel by activityViewModels<AddScheduleViewModel>()
    override fun onResume() {
        super.onResume()
        viewModel.activeStep.postValue(2)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun subscribeObservers() {

    }

}