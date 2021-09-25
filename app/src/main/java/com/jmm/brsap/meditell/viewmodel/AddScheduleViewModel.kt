package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmm.brsap.meditell.model.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AddScheduleViewModel :ViewModel(){

    val selectedDates = MutableLiveData<List<Schedule>>()
}