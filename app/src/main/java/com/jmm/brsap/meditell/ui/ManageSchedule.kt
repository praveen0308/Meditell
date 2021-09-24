package com.jmm.brsap.meditell.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jmm.brsap.meditell.adapters.ScheduleAdapter
import com.jmm.brsap.meditell.databinding.ActivityManageScheduleBinding
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class ManageSchedule : BaseActivity<ActivityManageScheduleBinding>(ActivityManageScheduleBinding::inflate),
    ScheduleAdapter.ScheduleInterface {


    private var  db = Firebase.firestore
    private val TAG = "ManageSchedule"
    private lateinit var scheduleAdapter:ScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRvSchedules()

        binding.fabAddNewSchedule.setOnClickListener {
            startActivity(Intent(this,AddSchedule::class.java))
        }

        lifecycleScope.launch {
            val result = db.collection("salesrepresentatives").document("2cbMfovcmRZINVueP1IS").collection("schedule").get().await()

            Log.d(TAG, "Result => $result")
            val schedules = result.toObjects(Schedule::class.java)
            scheduleAdapter.setScheduleList(schedules)
            for (document in schedules) {
                Log.d(TAG, "${document.date} => ${document.areaVisits.toString()}")
            }

        }

    }

    override fun subscribeObservers() {

    }

    private fun setupRvSchedules(){
        scheduleAdapter = ScheduleAdapter(this)
        binding.rvSchedule.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(context,
                layoutManager.orientation)
            addItemDecoration(dividerItemDecoration)

            this.layoutManager = layoutManager
            adapter =scheduleAdapter
        }
    }
}