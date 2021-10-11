package com.jmm.brsap.meditell.ui.schedule

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.ScheduleAdapter
import com.jmm.brsap.meditell.databinding.FragmentScheduleListBinding
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.MyDividerItemDecoration
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.ManageScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleList : BaseFragment<FragmentScheduleListBinding>(FragmentScheduleListBinding::inflate),
    ScheduleAdapter.ScheduleInterface {

    private val viewModel by viewModels<ManageScheduleViewModel>()

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var scheduleAdapter: ScheduleAdapter
    private var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvSchedules()

        binding.fabAddNewSchedule.setOnClickListener {
            startActivity(Intent(requireActivity(), AddSchedule::class.java))
        }
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
        binding.rvSchedule.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val decorator = MyDividerItemDecoration(AppCompatResources.getDrawable(context,R.drawable.rv_horizontal_line))
            addItemDecoration(decorator)

            this.layoutManager = layoutManager
            adapter = scheduleAdapter
        }
    }

    override fun onEditClick(item: Schedule) {
        findNavController().navigate(ScheduleListDirections.actionScheduleListToEditSchedule(item.date!!))
    }
}