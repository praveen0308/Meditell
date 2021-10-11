package com.jmm.brsap.meditell.ui.schedule

import android.R
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.adapters.CurrentDateAreaVisitAdapter
import com.jmm.brsap.meditell.adapters.SelectedAreaAdapter
import com.jmm.brsap.meditell.databinding.FragmentEditScheduleBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.City
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.util.convertDMY2STD
import com.jmm.brsap.meditell.viewmodel.ManageScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditSchedule :
    BaseFragment<FragmentEditScheduleBinding>(FragmentEditScheduleBinding::inflate),
    SelectedAreaAdapter.SelectedAreaInterface {

    private val viewModel by viewModels<ManageScheduleViewModel>()

    private val navArgs by navArgs<EditScheduleArgs>()
    private var scheduleId = ""
    private var userId = ""
    private var selectedAreaIds = mutableListOf<Int>()
    private var scheduleAreas = mutableListOf<Area>()

    private lateinit var selectedAreaAdapter: SelectedAreaAdapter
    private var selectedArea = Area()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleId = navArgs.scheduleId
        setupRvAreas()
        binding.tvDate.text = convertDMY2STD(scheduleId)
        viewModel.getCities()

        binding.btnAdd.setOnClickListener {
            var alreadyExist = false
            selectedAreaIds.forEach {
                if (it == selectedArea.areaId) alreadyExist = true
            }
            if (alreadyExist) showToast("already added")
            else {

                selectedAreaIds.add(selectedArea.areaId!!)
                scheduleAreas.add(
                    Area(
                        areaId = selectedArea.areaId!!,
                        name = selectedArea.name!!,
                        addressInfo = selectedArea.addressInfo!!
                    )
                )
            }

            selectedAreaAdapter.setAreaList(scheduleAreas)
        }

    }

    override fun subscribeObservers() {

        viewModel.userId.observe(viewLifecycleOwner, {
            userId = it
            viewModel.getCurrentDayAreas(userId, scheduleId)
        })

        viewModel.areas.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let { areas ->
                        selectedAreaIds.clear()
                        val ids = areas.map {
                            it.areaId!!
                        }
                        selectedAreaIds.addAll(ids.toMutableList())
                        scheduleAreas.clear()
                        scheduleAreas.addAll(areas)
                        selectedAreaAdapter.setAreaList(scheduleAreas)
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

        viewModel.areaList.observe(viewLifecycleOwner, { _result ->
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


        viewModel.updatedSchedule.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        showToast("Schedule updated successfully !!!")
                        findNavController().navigateUp()
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


    private fun populateCityAdapter(cityList: MutableList<City>) {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, cityList)
        binding.actvCities.threshold = 1 //start searching for values after typing first character
        binding.actvCities.setAdapter(arrayAdapter)

        binding.actvCities.setOnItemClickListener { parent, view, position, id ->
            val city = parent.getItemAtPosition(position) as City
            viewModel.getAreas(city.cityId!!)
        }
    }

    private fun populateAreasAdapter(cityList: MutableList<Area>) {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, cityList)
        binding.actvAreas.threshold = 1 //start searching for values after typing first character
        binding.actvAreas.setAdapter(arrayAdapter)

        binding.actvAreas.setOnItemClickListener { parent, view, position, id ->
            val area = parent.getItemAtPosition(position) as Area
            selectedArea = area
        }
    }

    private fun setupRvAreas() {
        selectedAreaAdapter = SelectedAreaAdapter(this)
        binding.rvLocations.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                layoutManager.orientation
            )
            addItemDecoration(dividerItemDecoration)

            this.layoutManager = layoutManager
            adapter = selectedAreaAdapter
        }
    }


    override fun onRemoveClick(item: Area) {
        for (i in 0 until scheduleAreas.size) {
            if (scheduleAreas[i].areaId == item.areaId) {
                scheduleAreas.removeAt(i)
            }

        }

        for (i in 0 until selectedAreaIds.size) {
            if (selectedAreaIds[i] == item.areaId) {
                selectedAreaIds.removeAt(i)
            }
        }
    }
}