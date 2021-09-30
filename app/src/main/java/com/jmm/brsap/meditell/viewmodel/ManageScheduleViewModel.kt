package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.*
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.City
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.repository.AreaRepository
import com.jmm.brsap.meditell.repository.AuthRepository
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
class ManageScheduleViewModel @Inject constructor(
    val authRepository: AuthRepository,
    private val areaRepository: AreaRepository,
    private val salesRepresentativeRepository: SalesRepresentativeRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) :ViewModel(){
    val userId = userPreferencesRepository.userId.asLiveData()

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
}