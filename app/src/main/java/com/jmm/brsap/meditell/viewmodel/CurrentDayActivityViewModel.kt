package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jmm.brsap.meditell.repository.SalesRepresentativeRepository
import com.jmm.brsap.meditell.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrentDayActivityViewModel @Inject constructor(
    private val salesRepresentativeRepository: SalesRepresentativeRepository,
    private val userPreferencesRepository: UserPreferencesRepository
):ViewModel(){

    val userId = userPreferencesRepository.userId.asLiveData()
}