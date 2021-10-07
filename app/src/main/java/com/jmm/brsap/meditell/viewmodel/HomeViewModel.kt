package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.*
import com.jmm.brsap.meditell.model.ReportModel
import com.jmm.brsap.meditell.model.SalesRepresentative
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
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val salesRepresentativeRepository: SalesRepresentativeRepository
) : ViewModel() {

    val userName = userPreferencesRepository.userName.asLiveData()
    val userId = userPreferencesRepository.userId.asLiveData()
    val welcomeStatus = userPreferencesRepository.welcomeStatus.asLiveData()

    var selectedFileUrl = ""

    fun clearUserInfo() {
        viewModelScope.launch {
            userPreferencesRepository.clearUserInfo()
        }

    }

    fun updateWelcomeStatus(status: Int) = viewModelScope.launch {
        userPreferencesRepository.updateWelcomeStatus(status)
    }

    fun updateUserName(userName: String) = viewModelScope.launch {
        userPreferencesRepository.updateUserName(userName)
    }

    fun updateUserId(userId: String) = viewModelScope.launch {
        userPreferencesRepository.updateUserId(userId)
    }

    fun updateFirstName(firstName: String) = viewModelScope.launch {
        userPreferencesRepository.updateUserFirstName(firstName)
    }

    fun updateLastName(lastName: String) = viewModelScope.launch {
        userPreferencesRepository.updateUserLastName(lastName)
    }

    private val _markAttendanceResponse = MutableLiveData<Resource<Boolean>>()
    val markAttendanceResponse: LiveData<Resource<Boolean>> = _markAttendanceResponse

    fun markAttendanceForTheDay(
        userId: String,
        scheduleDate: String,
        dateTime: String,
        action: String
    ) {
        viewModelScope.launch {
            salesRepresentativeRepository
                .markAttendance(userId, scheduleDate, dateTime, action)
                .onStart {
                    _markAttendanceResponse.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _markAttendanceResponse.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    if (it) _markAttendanceResponse.postValue(Resource.Success(it))
                    else _markAttendanceResponse.postValue(Resource.Error("Something went wrong !!!"))
                }
        }
    }


    private val _reportSubmitted = MutableLiveData<Resource<Boolean>>()
    val reportSubmitted: LiveData<Resource<Boolean>> = _reportSubmitted

    fun submitReport(
        userId: String,
        reportModel: ReportModel
    ) {
        viewModelScope.launch {
            salesRepresentativeRepository
                .submitReport(userId, reportModel)
                .onStart {
                    _reportSubmitted.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _reportSubmitted.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    if (it) _reportSubmitted.postValue(Resource.Success(it))
                    else _reportSubmitted.postValue(Resource.Error("Something went wrong !!!"))
                }
        }
    }

}