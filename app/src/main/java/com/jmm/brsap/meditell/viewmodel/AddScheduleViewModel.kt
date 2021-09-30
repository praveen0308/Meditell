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
class AddScheduleViewModel @Inject constructor(
    val authRepository: AuthRepository,
    private val areaRepository: AreaRepository,
    private val salesRepresentativeRepository: SalesRepresentativeRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) :ViewModel(){
    val userId = userPreferencesRepository.userId.asLiveData()
    val activeStep = MutableLiveData(0)
    val activeDay = MutableLiveData(0)
    val mSchedule = MutableLiveData<List<Schedule>>()

    var scheduleList= listOf<Schedule>()

    private val _cities = MutableLiveData<Resource<List<City>>>()
    val cities: LiveData<Resource<List<City>>> = _cities

    fun getCities() {
        viewModelScope.launch {
            areaRepository
                .getCities()
                .onStart {
                    _cities.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _cities.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _cities.postValue(Resource.Success(it))
                }
        }
    }

    private val _areas = MutableLiveData<Resource<List<Area>>>()
    val areas: LiveData<Resource<List<Area>>> = _areas

    fun getAreas(cityId:Int=0) {
        viewModelScope.launch {
            areaRepository
                .getAreas(cityId)
                .onStart {
                    _areas.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _areas.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _areas.postValue(Resource.Success(it))

                }
        }
    }

    private val _scheduleAdded = MutableLiveData<Resource<Boolean>>()
    val scheduleAdded: LiveData<Resource<Boolean>> = _scheduleAdded

    fun addNewSchedules(userId:String,schedules:List<Schedule>) {
        viewModelScope.launch {
            salesRepresentativeRepository
                .addSchedule(userId,schedules)
                .onStart {
                    _scheduleAdded.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _scheduleAdded.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _scheduleAdded.postValue(Resource.Success(it))

                }
        }
    }
}