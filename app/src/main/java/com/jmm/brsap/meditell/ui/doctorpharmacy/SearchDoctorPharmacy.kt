package com.jmm.brsap.meditell.ui.doctorpharmacy

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.adapters.CurrentAreaPersonAdapter
import com.jmm.brsap.meditell.databinding.ActivitySearchDoctorPharmacyBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.City
import com.jmm.brsap.meditell.util.BaseActivity
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.SearchDoctorOrPharmacyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDoctorPharmacy :
    BaseActivity<ActivitySearchDoctorPharmacyBinding>(ActivitySearchDoctorPharmacyBinding::inflate),
    CurrentAreaPersonAdapter.CurrentAreaPersonInterface {

    private val viewModel by viewModels<SearchDoctorOrPharmacyViewModel>()
    private var selectedArea = 0

    private lateinit var currentAreaPersonAdapter: CurrentAreaPersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRvPersons()
        viewModel.getDoctorPharmacies()
        viewModel.getCities()

        binding.btnSearch.setOnClickListener {
            viewModel.getDoctorPharmacies(selectedArea)
        }
    }

    override fun subscribeObservers() {
        viewModel.doctorsPharmacies.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        currentAreaPersonAdapter.setDoctorList(it)
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

        viewModel.areas.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        populateAreasAdapter(it.toMutableList())
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

    private fun populateCityAdapter(cityList: MutableList<City>) {
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, cityList)
        binding.actvCities.threshold = 1 //start searching for values after typing first character
        binding.actvCities.setAdapter(arrayAdapter)

        binding.actvCities.setOnItemClickListener { parent, view, position, id ->
            val city = parent.getItemAtPosition(position) as City
            viewModel.getAreas(city.cityId!!)
        }
    }

    private fun populateAreasAdapter(cityList: MutableList<Area>) {
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, cityList)
        binding.actvAreas.threshold = 1 //start searching for values after typing first character
        binding.actvAreas.setAdapter(arrayAdapter)

        binding.actvAreas.setOnItemClickListener { parent, view, position, id ->
            val area = parent.getItemAtPosition(position) as Area
            selectedArea = area.areaId!!
        }
    }

    private fun setupRvPersons() {
        currentAreaPersonAdapter = CurrentAreaPersonAdapter(this)
        binding.rvData.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                layoutManager.orientation
            )
            addItemDecoration(dividerItemDecoration)

            this.layoutManager = layoutManager
            adapter = currentAreaPersonAdapter
        }
    }

    override fun onRowClick(item: Any) {

    }


}