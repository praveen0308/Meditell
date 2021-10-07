package com.jmm.brsap.meditell.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.DailyCallRecordingAdapter
import com.jmm.brsap.meditell.adapters.DailySummaryLocationAdapter
import com.jmm.brsap.meditell.databinding.FragmentDailyCallRecordingBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.MyDividerItemDecoration
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.DailyCallRecordingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyCallRecording : BaseFragment<FragmentDailyCallRecordingBinding>(FragmentDailyCallRecordingBinding::inflate),
    DailySummaryLocationAdapter.DailySummaryInterface {

    private val viewModel by activityViewModels<DailyCallRecordingViewModel>()
    private lateinit var dailyCallRecordingAdapter: DailyCallRecordingAdapter
    private var userId = ""
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainDashboard).setToolbarTitle("Daily Call Recording")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvSchedules()

    }

    override fun subscribeObservers() {

        viewModel.userId.observe(this, {
            userId = it
            viewModel.getSchedule(userId)
        })
        viewModel.schedules.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        dailyCallRecordingAdapter.setScheduleList(it)
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

    private fun setupRvSchedules() {
        dailyCallRecordingAdapter = DailyCallRecordingAdapter(this)
        binding.rvDailyCallRecordings.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val decorator = MyDividerItemDecoration(AppCompatResources.getDrawable(context,R.drawable.rv_horizontal_line))
            addItemDecoration(decorator)

            this.layoutManager = layoutManager
            adapter = dailyCallRecordingAdapter
        }
    }

    override fun onSummaryClick(item: Area) {
        findNavController().navigate(DailyCallRecordingDirections.actionDailyCallRecordingToCallRecordingSummary(item.areaId!!,item.dateVisit!!))
    }

}