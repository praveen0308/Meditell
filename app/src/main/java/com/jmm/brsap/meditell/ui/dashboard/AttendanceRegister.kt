package com.jmm.brsap.meditell.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.ScheduleAdapter
import com.jmm.brsap.meditell.databinding.FragmentAttendanceRegisterBinding
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.MyDividerItemDecoration
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.AttendanceRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttendanceRegister : BaseFragment<FragmentAttendanceRegisterBinding>(FragmentAttendanceRegisterBinding::inflate),
    ScheduleAdapter.ScheduleInterface {
    private val viewModel by viewModels<AttendanceRegisterViewModel>()
    private var userId =""
    private lateinit var scheduleAdapter: ScheduleAdapter
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainDashboard).setToolbarTitle("Attendance Register")
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
                        scheduleAdapter.setScheduleList(it)
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
        scheduleAdapter = ScheduleAdapter(this)
        binding.rvAttendance.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val decorator = MyDividerItemDecoration(AppCompatResources.getDrawable(context,R.drawable.rv_horizontal_line))
            addItemDecoration(decorator)

            this.layoutManager = layoutManager
            adapter = scheduleAdapter
        }
    }
}