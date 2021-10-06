package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.adapters.ChooseAreaAdapter
import com.jmm.brsap.meditell.databinding.FragmentChooseAreaBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.util.BaseBottomSheetDialogFragment
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.AddScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseArea : BaseBottomSheetDialogFragment<FragmentChooseAreaBinding>(FragmentChooseAreaBinding::inflate),
    ChooseAreaAdapter.ChooseAreaInterface {

    private val viewModel by activityViewModels<AddScheduleViewModel>()
//    private val viewModel by activityViewModels<ManageAreaViewModel>()


    private lateinit var chooseAreaAdapter: ChooseAreaAdapter
    private val areas = mutableListOf<Area>()
    private val selectedAreas = mutableListOf<Area>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvAreas()
        viewModel.getAreas()
        binding.btnSubmit.setOnClickListener {
//            val selectedAreas = areas.filter { it.isSelected }
            val selectedAreasMap = hashMapOf<Int,String>()
            for (area in selectedAreas){
                selectedAreasMap.put(area.areaId!!.toInt(),area.name!!)
            }
//            viewModel.scheduleList[viewModel.activeDay.value!!].areaVisits!!.putAll(selectedAreasMap)
            dismiss()
        }
        binding.btnAddNewArea.setOnClickListener {
            /*val sheet = AddNewLocation()
            sheet.show(parentFragmentManager,sheet.tag)*/
        }
    }
    override fun subscribeObservers() {
        viewModel.areas.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {fetchedAreas->
                        areas.clear()
                        areas.addAll(fetchedAreas)
                        val activeDaySchedule = viewModel.scheduleList.find{ it.isActive }!!
                        if (activeDaySchedule.areaVisits.isNullOrEmpty()){

                        }else{
                            for (visitedArea in activeDaySchedule.areaVisits!!){
//                                areas.forEach { it.isSelected = it.areaId==visitedArea.key }
                            }
                            selectedAreas.clear()
                            selectedAreas.addAll(areas.filter { it.isSelected })
                        }
                       chooseAreaAdapter.setAreaList(areas)
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
            if (_result.status==Status.SUCCESS){
                viewModel.getAreas()
            }

        })
    }

    private fun setupRvAreas(){
        chooseAreaAdapter = ChooseAreaAdapter(this)
        binding.rvAreas.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(context,
                layoutManager.orientation)
            addItemDecoration(dividerItemDecoration)

            this.layoutManager = layoutManager
            adapter = chooseAreaAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        displayLoading(false)

    }

    interface ChooseAreaActionCallback{
        fun selectionDone()
    }

    override fun addNewSelection(area: Area) {
        selectedAreas.add(area)
    }

    override fun removeOneSelection(area: Area) {
        selectedAreas.forEach {
            if (it.areaId==area.areaId) selectedAreas.remove(it)
        }
    }
}