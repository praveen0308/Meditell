package com.jmm.brsap.meditell.ui.schedule

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.ActivityAddScheduleBinding
import com.jmm.brsap.meditell.ui.welcome.MainDashboard
import com.jmm.brsap.meditell.util.BaseActivity
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.AddScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSchedule : BaseActivity<ActivityAddScheduleBinding>(ActivityAddScheduleBinding::inflate) {

    private val viewModel by viewModels<AddScheduleViewModel>()
    private lateinit var navController: NavController

    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = Navigation.findNavController(this,R.id.nav_host_add_schedule)
        binding.btnNext.setOnClickListener {
            when(viewModel.activeStep.value){
                0->{
                    navController.navigate(SelectScheduleDateDirections.actionSelectScheduleDateToCreateSchedule())
                }
                1->{
                    navController.navigate(CreateScheduleDirections.actionCreateScheduleToConfirmSchedule())
                }
                2->{
                    viewModel.addNewSchedules(userId,viewModel.scheduleList)
                }
            }
        }
    }

    override fun subscribeObservers() {
        viewModel.userId.observe(this,{
            userId =it
        })
        viewModel.activeStep.observe(this,{
            if (it==2){
                binding.btnNext.setText("Submit")
            }
        })
        viewModel.scheduleAdded.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                       if (it){
                           showToast("Schedule added successfully !!!")
                           finish()
                       }else{
                           showToast("Something went wrong!!!")
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
}