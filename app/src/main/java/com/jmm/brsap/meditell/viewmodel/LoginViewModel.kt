package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.*
import com.jmm.brsap.meditell.model.SalesRepresentative
import com.jmm.brsap.meditell.repository.AuthRepository
import com.jmm.brsap.meditell.repository.UserPreferencesRepository
import com.jmm.brsap.meditell.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel(){

    val userName = userPreferencesRepository.userName.asLiveData()
    val welcomeStatus = userPreferencesRepository.welcomeStatus.asLiveData()


    fun clearUserInfo(){
        viewModelScope.launch {
            userPreferencesRepository.clearUserInfo()
        }

    }

    fun updateWelcomeStatus(status:Int)=viewModelScope.launch {
        userPreferencesRepository.updateWelcomeStatus(status)
    }

    fun updateUserName(userName:String)=viewModelScope.launch {
        userPreferencesRepository.updateUserName(userName)
    }

    fun updateFirstName(firstName:String)=viewModelScope.launch {
        userPreferencesRepository.updateUserFirstName(firstName)
    }

    fun updateLastName(lastName:String)=viewModelScope.launch {
        userPreferencesRepository.updateUserLastName(lastName)
    }

    private val _isUserExists = MutableLiveData<Resource<Boolean>>()
    val isUserExists: LiveData<Resource<Boolean>> = _isUserExists

    fun checkAccountAlreadyExist(userName: String) {
        viewModelScope.launch {
            authRepository
                .checkUserExist(userName)
                .onStart {
                    _isUserExists.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _isUserExists.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    if (it)_isUserExists.postValue(Resource.Success(it))
                    else _isUserExists.postValue(Resource.Error("User doesn't exist !!!"))
                }
        }
    }

    private val _loginResponse = MutableLiveData<Resource<SalesRepresentative>>()
    val loginResponse: LiveData<Resource<SalesRepresentative>> = _loginResponse

    fun doLogin(userName: String,password:String) {
        viewModelScope.launch {
            authRepository
                .doLogin(userName,password)
                .onStart {
                    _loginResponse.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _loginResponse.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    if (it == null){
                        _loginResponse.postValue(Resource.Error("Incorrect username or password !!!"))
                    }else{
                        _loginResponse.postValue(Resource.Success(it))
                    }
                }
        }
    }
}