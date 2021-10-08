package com.jmm.brsap.meditell.ui.dashboard

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.jmm.brsap.meditell.databinding.FragmentAddNewAreaBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.City
import com.jmm.brsap.meditell.util.BaseActivity
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.ManageAreaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewLocation : BaseActivity<FragmentAddNewAreaBinding>(FragmentAddNewAreaBinding::inflate) {

    private val viewModel by viewModels<ManageAreaViewModel>()
    private var selectedCity = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCities()
        binding.apply {
            btnSubmitLocation.setOnClickListener {
                val areaName =etAreaName.text.toString().trim()
                val areaAddress =etAreaAddress.text.toString().trim()
                viewModel.addNewArea(Area(
                    name = areaName,
                    addressInfo = areaAddress,
                    active = true,
                    cityId = selectedCity
                ))
            }
        }

    }

    override fun subscribeObservers() {
        viewModel.cities.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                       populateCityAdapter(it.toMutableList())
                    }
                    displayLoading(false)
                }
                Status.LOADING -> {
                    displayLoading(true)
                }
                Status.ERROR -> {
                    displayLoading(false)
                    _result.message?.let {
                        displayError(it)
                    }
                }
            }
        })
        viewModel.isAdded.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        showToast("Added successfully !!!")
                        finish()
                    }
                    displayLoading(false)
                }
                Status.LOADING -> {
                    displayLoading(true)
                }
                Status.ERROR -> {
                    displayLoading(false)
                    _result.message?.let {
                        displayError(it)
                    }
                }
            }
        })
    }

    private fun populateCityAdapter(cityList:MutableList<City>){
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cityList)
        binding.actvCities.threshold = 1 //start searching for values after typing first character
        binding.actvCities.setAdapter(arrayAdapter)

        binding.actvCities.setOnItemClickListener { parent, view, position, id ->
            val city = parent.getItemAtPosition(position) as City
            selectedCity = city.cityId!!
        }
    }
}