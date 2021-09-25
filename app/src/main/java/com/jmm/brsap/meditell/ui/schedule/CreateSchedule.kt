package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentCreateScheduleBinding
import com.jmm.brsap.meditell.util.BaseFragment
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CreateSchedule : BaseFragment<FragmentCreateScheduleBinding>(FragmentCreateScheduleBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun subscribeObservers() {

    }

}