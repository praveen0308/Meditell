package com.jmm.brsap.meditell.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.DailySummaryDoctorPharmacyAdapter
import com.jmm.brsap.meditell.adapters.DailySummaryLocationAdapter
import com.jmm.brsap.meditell.databinding.FragmentCallRecordingSummaryBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.InteractionModel
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.MyDividerItemDecoration
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.DailyCallRecordingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallRecordingSummary : BaseFragment<FragmentCallRecordingSummaryBinding>(FragmentCallRecordingSummaryBinding::inflate),
    DailySummaryDoctorPharmacyAdapter.DailySummaryInterface {

    private val viewModel by activityViewModels<DailyCallRecordingViewModel>()
    private lateinit var dailySummaryDoctorPharmacyAdapter: DailySummaryDoctorPharmacyAdapter
    private var userId = ""

    private val navArgs by navArgs<CallRecordingSummaryArgs>()
    private var date = ""
    private var areaId = 0

    private val interactions = mutableListOf<InteractionModel>()


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainDashboard).setToolbarTitle("Call Recording")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        date = navArgs.date
        areaId = navArgs.areaId
        setupTabLayout()
        setupRvSummaries()
        viewModel.getInteractions(date,areaId, userId)
    }

    private fun setupTabLayout(){
        binding.apply {
            tabLayout.addTab(tabLayout.newTab().setText("Doctor"))
            tabLayout.addTab(tabLayout.newTab().setText("Pharmacy"))

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab!!.position==0){
                        viewModel.filter.postValue("doctor")
                    }else viewModel.filter.postValue("pharmacy")
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
        }


    }

    override fun subscribeObservers() {
        viewModel.userId.observe(this, {
            userId = it
            viewModel.getSchedule(userId)
        })

        viewModel.interactions.observe(this, {
            val filteredInteractions = interactions.filter { interaction->
                interaction.interactionWasWith == viewModel.filter.value
            }
            dailySummaryDoctorPharmacyAdapter.setAreaList(filteredInteractions)
        })
        viewModel.interactions.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        interactions.clear()
                        interactions.addAll(it)

                        val filteredInteractions = interactions.filter { interaction->
                            interaction.interactionWasWith == viewModel.filter.value
                        }
                        dailySummaryDoctorPharmacyAdapter.setAreaList(filteredInteractions)
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

    private fun setupRvSummaries() {
        dailySummaryDoctorPharmacyAdapter = DailySummaryDoctorPharmacyAdapter(this)
        binding.rvCallRecordings.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val decorator = MyDividerItemDecoration(AppCompatResources.getDrawable(context,R.drawable.rv_horizontal_line))
            addItemDecoration(decorator)

            this.layoutManager = layoutManager
            adapter = dailySummaryDoctorPharmacyAdapter
        }
    }


    override fun onSummaryClick(item: InteractionModel) {
        findNavController().navigate(CallRecordingSummaryDirections.actionCallRecordingSummaryToInteractionSummary(item.interactionId!!))
    }


}