package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import com.jmm.brsap.meditell.adapters.CurrentDateAreaVisitAdapter
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.City
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.Status


@AndroidEntryPoint
class CreateSchedule : BaseFragment<FragmentCreateScheduleBinding>(FragmentCreateScheduleBinding::inflate),
    ScheduleDateAdapter.ScheduleDateInterface,
    CurrentDateAreaVisitAdapter.CurrentDateAreaVisitInterface {

    private val viewModel by activityViewModels<AddScheduleViewModel>()
    private lateinit var scheduleDateAdapter :ScheduleDateAdapter
    private lateinit var scheduleVPAdapter: ScheduleVPAdapter
    private var mScheduleList = mutableListOf<Schedule>()

    private var selectedArea = Area()

    override fun onResume() {
        super.onResume()
        viewModel.activeStep.postValue(1)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvDates()
        setupVPSchedule()
        viewModel.getCities()

        binding.btnAdd.setOnClickListener {
            var alreadyExist = false
            viewModel.scheduleList[viewModel.activeDay.value!!].areaVisits!!.forEach {
                if (it == selectedArea.areaId) alreadyExist=true

            }
            if(alreadyExist) showToast("already added")
            else{

                viewModel.scheduleList[viewModel.activeDay.value!!].areaVisits!!.add(selectedArea.areaId!!)
            }

            scheduleVPAdapter.setScheduleList(viewModel.scheduleList)
        }
        binding.vpScheduleFrame.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                    viewModel.activeDay.postValue(position)
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
        scheduleDateAdapter.setModelDayList(viewModel.scheduleList)
    }

    private fun setupVPSchedule(){
        scheduleVPAdapter = ScheduleVPAdapter(this)
        binding.vpScheduleFrame.apply {
            adapter = scheduleVPAdapter
        }
        scheduleVPAdapter.setScheduleList(viewModel.scheduleList)

    }

    override fun subscribeObservers() {
        viewModel.activeDay.observe(viewLifecycleOwner,{activeDay->
            viewModel.scheduleList.forEach { it.isActive=false }
            viewModel.scheduleList[activeDay].isActive = true

            scheduleDateAdapter.setModelDayList(viewModel.scheduleList)
            binding.rvDaysList.scrollToPosition(activeDay)
        })

        viewModel.cities.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        populateCityAdapter(it.toMutableList())
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

        viewModel.areas.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        populateAreasAdapter(it.toMutableList())
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

    private fun populateCityAdapter(cityList:MutableList<City>){
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cityList)
        binding.actvCities.threshold = 1 //start searching for values after typing first character
        binding.actvCities.setAdapter(arrayAdapter)

        binding.actvCities.setOnItemClickListener { parent, view, position, id ->
            val city = parent.getItemAtPosition(position) as City
            if (viewModel.scheduleList[viewModel.activeDay.value!!].dayCityVisit == city.cityId){

            }
            else{
                viewModel.scheduleList[viewModel.activeDay.value!!].dayCityVisit = city.cityId!!
                viewModel.scheduleList[viewModel.activeDay.value!!].areaVisits!!.clear()
                viewModel.getAreas(city.cityId!!)
            }

        }
    }

    private fun populateAreasAdapter(cityList:MutableList<Area>){
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cityList)
        binding.actvAreas.threshold = 1 //start searching for values after typing first character
        binding.actvAreas.setAdapter(arrayAdapter)

        binding.actvAreas.setOnItemClickListener { parent, view, position, id ->
            val area = parent.getItemAtPosition(position) as Area
            selectedArea=area
        }
    }


    override fun onItemClick(item: Area) {

    }

}