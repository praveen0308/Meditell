package com.jmm.brsap.meditell.ui.doctorpharmacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentAddNewPharmacyBinding
import com.jmm.brsap.meditell.model.*
import com.jmm.brsap.meditell.util.BaseBottomSheetDialogFragment
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.util.getCurrentDateTime
import com.jmm.brsap.meditell.viewmodel.AddDoctorOrPharmacyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewPharmacy : BaseFragment<FragmentAddNewPharmacyBinding>(FragmentAddNewPharmacyBinding::inflate) {

    private val viewModel by activityViewModels<AddDoctorOrPharmacyViewModel>()
    private var selectedAreaId = 0
    private var selectedCityId = 0
    private var userId = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


            btnSubmit.setOnClickListener {
                val pharmacyName = etNameOfPharmacy.text.toString().trim()
                val address = etAddress.text.toString().trim()
                val contact = etContact.text.toString().trim()
                val pharmacistName = etPharmacistName.text.toString().trim()

                viewModel.addNewPharmacy(
                    Pharmacy(
                        pharmacyName = pharmacyName,
                        address = address,
                        pharmacistName = pharmacistName,
                        contactNo = contact,
                        cityId = selectedCityId,
                        areaId = selectedAreaId,
                        addedBy = userId,
                        addedOn = getCurrentDateTime()
                    )
                )
            }
        }
    }

    override fun subscribeObservers() {
        viewModel.userId.observe(this, {
            userId = it
        })

        viewModel.pharmacyAdded.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        if (it) {
                            showToast("Pharmacy added successfully !!!")
                            requireActivity().finish()
                        } else {
                            showToast("Something went wrong!!!")
                        }
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

        viewModel.cities.observe(viewLifecycleOwner, { _result ->
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

        viewModel.areas.observe(viewLifecycleOwner, { _result ->
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
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cityList)
        binding.actvCities.threshold = 1 //start searching for values after typing first character
        binding.actvCities.setAdapter(arrayAdapter)

        binding.actvCities.setOnItemClickListener { parent, view, position, id ->
            val city = parent.getItemAtPosition(position) as City
            selectedAreaId = city.cityId!!
            viewModel.getAreas(selectedCityId)

        }
    }

    private fun populateAreasAdapter(cityList: MutableList<Area>) {
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cityList)
        binding.actvAreas.threshold = 1 //start searching for values after typing first character
        binding.actvAreas.setAdapter(arrayAdapter)

        binding.actvAreas.setOnItemClickListener { parent, view, position, id ->
            val area = parent.getItemAtPosition(position) as Area
            selectedAreaId = area.areaId!!
        }
    }


}