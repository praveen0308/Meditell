package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.*
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.InteractionModel
import com.jmm.brsap.meditell.repository.InteractionRepository
import com.jmm.brsap.meditell.repository.SalesRepresentativeRepository
import com.jmm.brsap.meditell.repository.UserPreferencesRepository
import com.jmm.brsap.meditell.util.InteractionType
import com.jmm.brsap.meditell.util.Resource
import com.jmm.brsap.meditell.util.UserTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentDayActivityViewModel @Inject constructor(
    private val salesRepresentativeRepository: SalesRepresentativeRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val interactionRepository: InteractionRepository
):ViewModel(){

    val userId = userPreferencesRepository.userId.asLiveData()
    var selectedAreaId = 0
    var selectedInteractedWith = "doctor"
    var selectedInteractiveInfo = ""
    var interactionType = "call"
    var selectedInteractedId = 0
    var selectedImageUrl = ""



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

    private val _areaPersons = MutableLiveData<Resource<List<Any>>>()
    val areaPersons: LiveData<Resource<List<Any>>> = _areaPersons

    fun getCurrentAreaDoctorsAndPharmacy(areaId:Int=selectedAreaId) {
        viewModelScope.launch {
            salesRepresentativeRepository
                .getCurrentAreaDoctorsAndPharmacy(areaId)
                .onStart {
                    _areaPersons.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _areaPersons.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _areaPersons.postValue(Resource.Success(it))

                }
        }
    }

    private val _isInteractionAdded = MutableLiveData<Resource<Boolean>>()
    val isInteractionAdded: LiveData<Resource<Boolean>> = _isInteractionAdded

    fun addNewInteraction(interactionModel: InteractionModel) {
        viewModelScope.launch {
            interactionRepository
                .addNewInteraction(interactionModel)
                .onStart {
                    _isInteractionAdded.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _isInteractionAdded.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _isInteractionAdded.postValue(Resource.Success(it))

                }
        }
    }
}