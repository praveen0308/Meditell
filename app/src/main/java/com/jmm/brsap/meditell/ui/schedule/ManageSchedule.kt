package com.jmm.brsap.meditell.ui.schedule

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.ScheduleAdapter
import com.jmm.brsap.meditell.databinding.ActivityManageScheduleBinding
import com.jmm.brsap.meditell.util.BaseActivity
import com.jmm.brsap.meditell.util.MyDividerItemDecoration
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.ManageScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.security.AccessController.getContext
import javax.inject.Inject


@AndroidEntryPoint
class ManageSchedule :
    BaseActivity<ActivityManageScheduleBinding>(ActivityManageScheduleBinding::inflate),
    ScheduleAdapter.ScheduleInterface {

    private val viewModel by viewModels<ManageScheduleViewModel>()

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var scheduleAdapter: ScheduleAdapter
    private var userId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRvSchedules()

        binding.fabAddNewSchedule.setOnClickListener {
            startActivity(Intent(this, AddSchedule::class.java))
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
}