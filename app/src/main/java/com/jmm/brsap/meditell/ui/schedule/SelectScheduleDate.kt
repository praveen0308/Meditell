package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.ScheduleDateAdapter
import com.jmm.brsap.meditell.databinding.FragmentSelectScheduleDateBinding
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.*
import com.jmm.brsap.meditell.viewmodel.AddScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class SelectScheduleDate :
    BaseFragment<FragmentSelectScheduleDateBinding>(FragmentSelectScheduleDateBinding::inflate),
    ScheduleDateAdapter.ScheduleDateInterface {

    private val viewModel by activityViewModels<AddScheduleViewModel>()
    private lateinit var scheduleDateAdapter: ScheduleDateAdapter

    private var startDate = ""
    private var endDate = ""

    private var selectedDate = NavigationEnum.SINGLE_DAY
    override fun onResume() {
        super.onResume()
        viewModel.activeStep.postValue(0)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRvSelectedDates()
        binding.apply {
            cdSingleDay.setOnClickListener {
                viewModel.selectedScheduleType.value = NavigationEnum.SINGLE_DAY
            }

            cdWeekly.setOnClickListener {
                viewModel.selectedScheduleType.value = NavigationEnum.WEEKLY
            }
        }
        binding.btnSelectDates.setOnClickListener {

            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
            if (viewModel.selectedScheduleType.value==NavigationEnum.SINGLE_DAY) {
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

                    val schedules = mutableListOf<Schedule>()
                    schedules.add(Schedule(date = selectedDate))
                    viewModel.scheduleList=schedules
                    somethingChanged()
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
//                    binding.tvSelectedDates.text = "from $startDate to $endDate"
                    val dates = getDaysBetweenDates(startDate, endDate)
                    val schedules = mutableListOf<Schedule>()
                    for (day in dates) {
                        schedules.add(Schedule(date = day,areaVisits = mutableListOf()))
                    }
                    viewModel.scheduleList=schedules
                    somethingChanged()
//                    viewModel.mSchedule.postValue(schedules)

                }
            }
        }
    }

    private fun somethingChanged(){
        viewModel.scheduleList.let {
            if (it.isNullOrEmpty()) {
                binding.apply {
                    lblSelectedDates.isVisible = false
                    rvSelectedDates.isVisible = false
                    tvNoOfDaysSelected.isVisible = false
                    textView13.isVisible =false
                }
            } else if (it.size == 1) {
                binding.apply {
                    lblSelectedDates.isVisible = true
                    rvSelectedDates.isVisible = true
                    tvNoOfDaysSelected.isVisible = true
                    textView13.isVisible =true

                    tvNoOfDaysSelected.text = "${it.size} Day"
                    scheduleDateAdapter.setModelDayList(viewModel.scheduleList)
                }
            } else {
                binding.apply {
                    lblSelectedDates.isVisible = true
                    rvSelectedDates.isVisible = true
                    tvNoOfDaysSelected.isVisible = true
                    textView13.isVisible =true
                    tvNoOfDaysSelected.text = "${it.size} Days"
                    scheduleDateAdapter.setModelDayList(viewModel.scheduleList)
                }
            }
        }
    }
    override fun subscribeObservers() {
        viewModel.selectedScheduleType.observe(viewLifecycleOwner,{

            if (it==NavigationEnum.SINGLE_DAY){
                showToast("Single day")
                binding.apply {
                    cdSingleDay.strokeColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                    cdSingleDay.strokeWidth = 5
                    cdWeekly.strokeWidth = 0
                    cdWeekly.setStrokeColor(null)
                    cdWeekly.strokeColor = ContextCompat.getColor(requireContext(),R.color.material_on_surface_stroke)


                }
            }else{
                showToast("Weekly")
                binding.apply {
                    cdSingleDay.setStrokeColor(null)
                    cdSingleDay.strokeColor = ContextCompat.getColor(requireContext(),R.color.material_on_surface_stroke)
                    cdSingleDay.strokeWidth = 0
                    cdWeekly.strokeColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                    cdWeekly.strokeWidth = 5
                }
            }

        })
        viewModel.userId.observe(viewLifecycleOwner,{
            Timber.d("UserID : $it")
        })
    }

    private fun setUpRvSelectedDates(){
        scheduleDateAdapter = ScheduleDateAdapter(this,1)
        binding.rvSelectedDates.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context,4)
            adapter = scheduleDateAdapter
        }
    }

    override fun onDayClick(position: Int, item: Schedule) {

    }

}