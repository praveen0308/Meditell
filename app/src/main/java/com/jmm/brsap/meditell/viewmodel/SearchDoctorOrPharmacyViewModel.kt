package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.*
import com.jmm.brsap.meditell.model.*
import com.jmm.brsap.meditell.repository.AreaRepository
import com.jmm.brsap.meditell.repository.DoctorRepository
import com.jmm.brsap.meditell.repository.PharmacyRepository
import com.jmm.brsap.meditell.repository.UserPreferencesRepository
import com.jmm.brsap.meditell.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDoctorOrPharmacyViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    private val pharmacyRepository: PharmacyRepository,
    private val areaRepository: AreaRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) :ViewModel(){
    val userId = userPreferencesRepository.userId.asLiveData()

    private val _doctorsPharmacies = MutableLiveData<Resource<List<Any>>>()
    val doctorsPharmacies: LiveData<Resource<List<Any>>> = _doctorsPharmacies

    fun getDoctorPharmacies(areaId:Int=0) {
        viewModelScope.launch {
            doctorRepository
                .getDoctorsAndPharmacy(areaId)
                .onStart {
                    _doctorsPharmacies.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _doctorsPharmacies.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _doctorsPharmacies.postValue(Resource.Success(it))

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


}