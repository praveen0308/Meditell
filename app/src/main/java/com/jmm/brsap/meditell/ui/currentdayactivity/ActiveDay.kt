package com.jmm.brsap.meditell.ui.currentdayactivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.adapters.CurrentDateAreaVisitAdapter
import com.jmm.brsap.meditell.databinding.FragmentActiveDayBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.util.*
import com.jmm.brsap.meditell.viewmodel.CurrentDayActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ActiveDay : BaseFragment<FragmentActiveDayBinding>(FragmentActiveDayBinding::inflate),
    CurrentDateAreaVisitAdapter.CurrentDateAreaVisitInterface {

    private val viewModel by activityViewModels<CurrentDayActivityViewModel>()
    private lateinit var currentDateAreaVisitAdapter: CurrentDateAreaVisitAdapter
    private var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvAreas()
        binding.tvTime.text = convertDMY2EMDY(getTodayDate())
        binding.btnEndDay.setOnClickListener {
            viewModel.markAttendanceForTheDay(userId, getTodayDate(),
                getCurrentDateTime(), FirebaseDB.CHECKIN)
        }
        binding.fabAddNewSchedule.setOnClickListener {

        }
    }

    override fun subscribeObservers() {
        viewModel.userId.observe(viewLifecycleOwner,{
            userId = it
            viewModel.getCurrentDayAreas(userId, getTodayDate())
        })

        viewModel.areas.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {areas->
                      currentDateAreaVisitAdapter.setAreaList(areas)
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

        viewModel.markAttendanceResponse.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        if (it){
                            val intent = Intent(requireActivity(),CurrentActiveDayActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

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
    private fun setupRvAreas(){
        currentDateAreaVisitAdapter = CurrentDateAreaVisitAdapter(this)
        binding.rvAreas.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(context,
                layoutManager.orientation)
            addItemDecoration(dividerItemDecoration)

            this.layoutManager = layoutManager
            adapter = currentDateAreaVisitAdapter
        }
    }

    override fun onItemClick(item: Area) {
        viewModel.selectedAreaId = item.areaId!!
        findNavController().navigate(ActiveDayDirections.actionActiveDay2ToSelectInteractedWith2())
    }
}