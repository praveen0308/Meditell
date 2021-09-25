package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    val authRepository: AuthRepository
) :ViewModel(){

    val selectedDates = MutableLiveData<List<Schedule>>()
}