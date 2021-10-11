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
class AddDoctorOrPharmacyViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    private val pharmacyRepository: PharmacyRepository,
    private val areaRepository: AreaRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) :ViewModel(){
    val userId = userPreferencesRepository.userId.asLiveData()
    val activeTab = MutableLiveData(0)


    private val _degrees = MutableLiveData<Resource<List<Degree>>>()
    val degrees: LiveData<Resource<List<Degree>>> = _degrees

    fun getDegrees() {
        viewModelScope.launch {
            doctorRepository
                .getDegrees()
                .onStart {
                    _degrees.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _degrees.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _degrees.postValue(Resource.Success(it))
                }
        }
    }


    private val _doctorAdded = MutableLiveData<Resource<Boolean>>()
    val doctorAdded: LiveData<Resource<Boolean>> = _doctorAdded

    fun addNewDoctor(doctor:Doctor) {
        viewModelScope.launch {
            doctorRepository
                .addNewDoctor(doctor)
                .onStart {
                    _doctorAdded.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _doctorAdded.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _doctorAdded.postValue(Resource.Success(it))
                }
        }
    }

    private val _degreeAdded = MutableLiveData<Resource<Int>>()
    val degreeAdded: LiveData<Resource<Int>> = _degreeAdded

    fun addNewDegree(degree: Degree) {
        viewModelScope.launch {
            doctorRepository
                .addNewDegree(degree)
                .onStart {
                    _degreeAdded.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _degreeAdded.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _degreeAdded.postValue(Resource.Success(it))
                }
        }
    }

    private val _pharmacyAdded = MutableLiveData<Resource<Boolean>>()
    val pharmacyAdded: LiveData<Resource<Boolean>> = _pharmacyAdded

    fun addNewPharmacy(pharmacy:Pharmacy) {
        viewModelScope.launch {
            pharmacyRepository
                .addNewPharmacy(pharmacy)
                .onStart {
                    _pharmacyAdded.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _pharmacyAdded.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _pharmacyAdded.postValue(Resource.Success(it))
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