package com.jmm.brsap.meditell.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.repository.AreaRepository
import com.jmm.brsap.meditell.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAreaViewModel @Inject constructor(
    private val areaRepository: AreaRepository
):ViewModel() {

    private val _areas = MutableLiveData<Resource<List<Area>>>()
    val areas: LiveData<Resource<List<Area>>> = _areas

    fun getAreas() {
        viewModelScope.launch {
            areaRepository
                .getAreas()
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

    private val _isAdded = MutableLiveData<Resource<Boolean>>()
    val isAdded: LiveData<Resource<Boolean>> = _isAdded

    fun addNewArea(area: Area) {
        viewModelScope.launch {
            areaRepository
                .addNewArea(area)
                .onStart {
                    _isAdded.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _isAdded.postValue(Resource.Error(it))
                    }
                }
                .collect {
                    _isAdded.postValue(Resource.Success(it))

                }
        }
    }
}