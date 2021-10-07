package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.*
import com.jmm.brsap.meditell.model.InteractionModel
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.repository.InteractionRepository
import com.jmm.brsap.meditell.repository.SalesRepresentativeRepository
import com.jmm.brsap.meditell.repository.UserPreferencesRepository
import com.jmm.brsap.meditell.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyCallRecordingViewModel @Inject constructor(
    private val salesRepresentativeRepository: SalesRepresentativeRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val interactionRepository: InteractionRepository
) :ViewModel(){
    val userId = userPreferencesRepository.userId.asLiveData()
    val filter = MutableLiveData("doctor")
    private val _schedules = MutableLiveData<Resource<List<Schedule>>>()
    val schedules: LiveData<Resource<List<Schedule>>> = _schedules

    fun getSchedule(userId:String) {
        viewModelScope.launch {
            salesRepresentativeRepository
                .getSchedule(userId)
                .onStart {
                    _schedules.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _schedules.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _schedules.postValue(Resource.Success(it))

                }
        }
    }

    private val _interactions = MutableLiveData<Resource<List<InteractionModel>>>()
    val interactions: LiveData<Resource<List<InteractionModel>>> = _interactions

    fun getInteractions(date:String,areaId:Int,userId:String) {
        viewModelScope.launch {
            interactionRepository
                .getInteractionOfTheDay(date, areaId, userId)
                .onStart {
                    _interactions.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _interactions.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _interactions.postValue(Resource.Success(it))

                }
        }
    }

    private val _interactionSummary = MutableLiveData<Resource<InteractionModel>>()
    val interactionSummary: LiveData<Resource<InteractionModel>> = _interactionSummary

    fun getInteractionSummary(interactionId:Int) {
        viewModelScope.launch {
            interactionRepository
                .getInteractionDetail(interactionId)
                .onStart {
                    _interactionSummary.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _interactionSummary.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _interactionSummary.postValue(Resource.Success(it))

                }
        }
    }
}