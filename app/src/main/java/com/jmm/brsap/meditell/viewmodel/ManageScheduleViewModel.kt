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

    private val _areas = MutableLiveData<Resource<List<Area>>>()
    val areas: LiveData<Resource<List<Area>>> = _areas

    fun getCurrentDayAreas(userId:String,scheduleDate:String) {
        viewModelScope.launch {
            salesRepresentativeRepository
                .getCurrentDayAreas(userId, scheduleDate)
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

    private val _areaList = MutableLiveData<Resource<List<Area>>>()
    val areaList: LiveData<Resource<List<Area>>> = _areaList

    fun getAreas(cityId:Int=0) {
        viewModelScope.launch {
            areaRepository
                .getAreas(cityId)
                .onStart {
                    _areaList.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _areaList.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _areaList.postValue(Resource.Success(it))

                }
        }
    }

    private val _updatedSchedule = MutableLiveData<Resource<Boolean>>()
    val updatedSchedule: LiveData<Resource<Boolean>> = _updatedSchedule

    fun updateDaySchedule(userId:String,date:String,areas:List<Int>) {
        viewModelScope.launch {
            salesRepresentativeRepository
                .updateDaySchedule(userId,date, areas)
                .onStart {
                    _updatedSchedule.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _updatedSchedule.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _updatedSchedule.postValue(Resource.Success(it))

                }
        }
    }

}