package com.jmm.brsap.meditell.ui.doctorpharmacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.ActivityAddDoctorOrPharmacyBinding
import com.jmm.brsap.meditell.databinding.FragmentAddNewDoctorBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.City
import com.jmm.brsap.meditell.model.Degree
import com.jmm.brsap.meditell.model.Doctor
import com.jmm.brsap.meditell.util.*
import com.jmm.brsap.meditell.viewmodel.AddDoctorOrPharmacyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.selects.select
import java.util.*

@AndroidEntryPoint
class AddNewDoctor :
    BaseFragment<FragmentAddNewDoctorBinding>(FragmentAddNewDoctorBinding::inflate) {

    private val viewModel by activityViewModels<AddDoctorOrPharmacyViewModel>()

    private var selectedAreaId = 0
    private var selectedCityId = 0
    private var selectedDegreeId = 0
    private var dob = ""
    private var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCities()
        viewModel.getDegrees()

        binding.apply {


            btnSubmit.setOnClickListener {
                val doctorName = etDoctorName.text.toString().trim()
                val clinicAddress = etClinicAddress.text.toString().trim()
                val contact = etContact.text.toString().trim()

                viewModel.addNewDoctor(
                    Doctor(
                        name = doctorName,
                        address = clinicAddress,
                        contactNo = contact,
                        cityId = selectedCityId,
                        areaId = selectedAreaId,
                        degree = selectedDegreeId,
                        dateOfBirth = dob,
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

        viewModel.doctorAdded.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        if (it) {
                            showToast("Doctor added successfully !!!")
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

        viewModel.degrees.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        populateDegreeAdapter(it.toMutableList())
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

    private fun populateDegreeAdapter(degrees: MutableList<Degree>) {
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, degrees)
        binding.actvDegree.threshold = 1 //start searching for values after typing first character
        binding.actvDegree.setAdapter(arrayAdapter)

        binding.actvDegree.setOnItemClickListener { parent, view, position, id ->
            val degree = parent.getItemAtPosition(position) as Degree
            selectedDegreeId = degree.degreeId!!

        }
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