package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentSelectScheduleDateBinding
import com.jmm.brsap.meditell.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SelectScheduleDate : BaseFragment<FragmentSelectScheduleDateBinding>(FragmentSelectScheduleDateBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelectDates.setOnClickListener {

            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
            if (binding.rbSingleDay.isChecked){
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build()
                datePicker.show(parentFragmentManager,datePicker.tag)
                datePicker.addOnPositiveButtonClickListener {

                }
            }
            else{
                val datePicker =
                    MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select date")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build()
                datePicker.show(parentFragmentManager,datePicker.tag)

            }
        }
    }

    override fun subscribeObservers() {

    }

}