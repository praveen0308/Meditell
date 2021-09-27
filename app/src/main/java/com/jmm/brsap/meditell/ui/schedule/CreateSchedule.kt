package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentCreateScheduleBinding
import com.jmm.brsap.meditell.util.BaseFragment
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.jmm.brsap.meditell.adapters.ScheduleAdapter
import com.jmm.brsap.meditell.adapters.ScheduleDateAdapter
import com.jmm.brsap.meditell.adapters.ScheduleVPAdapter
import com.jmm.brsap.meditell.model.ModelDay
import com.jmm.brsap.meditell.viewmodel.AddScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.jmm.brsap.meditell.model.Schedule


@AndroidEntryPoint
class CreateSchedule : BaseFragment<FragmentCreateScheduleBinding>(FragmentCreateScheduleBinding::inflate),
    ScheduleDateAdapter.ScheduleDateInterface, ScheduleVPAdapter.ScheduleVPInterface {

    private val viewModel by activityViewModels<AddScheduleViewModel>()
    private lateinit var scheduleDateAdapter :ScheduleDateAdapter
    private lateinit var scheduleVPAdapter: ScheduleVPAdapter
    private var mScheduleList = mutableListOf<Schedule>()

    override fun onResume() {
        super.onResume()
        viewModel.activeStep.postValue(1)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvDates()
        setupVPSchedule()
        binding.btnSelectAreas.setOnClickListener {
            val sheet = ChooseArea()
            sheet.show(parentFragmentManager,sheet.tag)
        }
        binding.vpScheduleFrame.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                    mScheduleList.forEach { it.isActive=false }
                    mScheduleList[position].isActive = true
                    scheduleDateAdapter.setModelDayList(mScheduleList)

                    binding.rvDaysList.scrollToPosition(position)
//                    binding.txtDayName.setText(adapterDatesList.getActiveDayDate())


//                currentItem = position
            }
        })
    }

    private fun setupRvDates(){
        scheduleDateAdapter = ScheduleDateAdapter(this)
        binding.rvDaysList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = scheduleDateAdapter
        }
    }

    private fun setupVPSchedule(){
        scheduleVPAdapter = ScheduleVPAdapter(this)
        binding.vpScheduleFrame.apply {
            adapter = scheduleVPAdapter
        }
    }

    override fun subscribeObservers() {
        viewModel.mSchedule.observe(viewLifecycleOwner,{
            mScheduleList.clear()
            mScheduleList.addAll(it)
            /*val daysList = mScheduleList.map {schedule->
                ModelDay(date = schedule.date)
            }*/
            scheduleDateAdapter.setModelDayList(mScheduleList)
            scheduleVPAdapter.setScheduleList(mScheduleList)
        })
    }

}