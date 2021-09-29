package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentSelectScheduleDateBinding
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.convertEpochTimeToDate
import com.jmm.brsap.meditell.util.convertSecondsTimeToDate
import com.jmm.brsap.meditell.util.getDaysBetweenDates
import com.jmm.brsap.meditell.viewmodel.AddScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class SelectScheduleDate :
    BaseFragment<FragmentSelectScheduleDateBinding>(FragmentSelectScheduleDateBinding::inflate) {

    private val viewModel by activityViewModels<AddScheduleViewModel>()

    private var startDate = ""
    private var endDate = ""
    override fun onResume() {
        super.onResume()
        viewModel.activeStep.postValue(0)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelectDates.setOnClickListener {

            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
            if (binding.rbSingleDay.isChecked) {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build()
                datePicker.show(parentFragmentManager, datePicker.tag)
                datePicker.addOnPositiveButtonClickListener {

                }

                datePicker.addOnPositiveButtonClickListener {

                    val selectedDate = convertSecondsTimeToDate(it)
                    binding.lblSelectedDates.setText("Selected Date")
                    binding.tvSelectedDates.text = "from $startDate to $endDate"

                    val schedules = mutableListOf<Schedule>()
                    schedules.add(Schedule(date = selectedDate))
//                    viewModel.mSchedule.postValue(schedules)
                    viewModel.scheduleList=schedules

                }
            } else {
                val datePicker =
                    MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select date")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build()
                datePicker.show(parentFragmentManager, datePicker.tag)
                datePicker.addOnPositiveButtonClickListener {
                    startDate = convertSecondsTimeToDate(it.first)
                    endDate = convertSecondsTimeToDate(it.second)
                    Timber.d("from $startDate to $endDate")
                    Timber.d(getDaysBetweenDates(startDate, endDate).toString())
                    binding.tvSelectedDates.text = "from $startDate to $endDate"
                    val dates = getDaysBetweenDates(startDate, endDate)
                    val schedules = mutableListOf<Schedule>()
                    for (day in dates) {
                        schedules.add(Schedule(date = day,areaVisits = mutableListOf()))
                    }
                    viewModel.scheduleList=schedules
//                    viewModel.mSchedule.postValue(schedules)

                }
            }
        }
    }

    override fun subscribeObservers() {
        viewModel.mSchedule.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                binding.apply {
                    lblSelectedDates.isVisible = false
                    tvSelectedDates.isVisible = false
                }
            } else if (it.size == 1) {
                binding.apply {
                    lblSelectedDates.isVisible = true
                    lblSelectedDates.setText("Selected Date")
                    tvSelectedDates.isVisible = true
                    tvSelectedDates.text = it[0].date
                }
            } else {
                binding.apply {
                    lblSelectedDates.isVisible = true
                    lblSelectedDates.setText("Selected Dates")
                    tvSelectedDates.isVisible = true
                    tvSelectedDates.text = "from ${it[0].date} to ${it.last().date}"
                }
            }

        })
    }

}