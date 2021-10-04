package com.jmm.brsap.meditell.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.DashboardWidgetAdapter
import com.jmm.brsap.meditell.databinding.FragmentHomeBinding
import com.jmm.brsap.meditell.model.WidgetModel
import com.jmm.brsap.meditell.ui.currentdayactivity.CurrentActiveDayActivity
import com.jmm.brsap.meditell.ui.schedule.ManageSchedule
import com.jmm.brsap.meditell.util.*

import com.jmm.brsap.meditell.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class Home : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), DashboardWidgetAdapter.DashboardWidgetInterface {

    private val viewModel by viewModels<HomeViewModel>()
    
    private lateinit var dashboardWidgetAdapter: DashboardWidgetAdapter
    private var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvMenus()
        populateMenus()
        binding.tvTime.text = convertDMY2EMDY(getTodayDate())

        binding.btnStartDay.setOnClickListener {
            val currentTime =SDF_DMYHMS.format(Date()).toString()

            viewModel.markAttendanceForTheDay(userId, getTodayDate(),currentTime,FirebaseDB.CHECKIN)
//            findNavController().navigate(R.id.action_home2_to_activeDay)

        }
    }

    private fun populateMenus() {
        val widgetList= mutableListOf<WidgetModel>()
        widgetList.add(WidgetModel(NavigationEnum.MANAGE_SCHEDULE,"Manage Schedule", R.drawable.ic_manage_schedule))
        widgetList.add(WidgetModel(NavigationEnum.ADD_NEW_LOCATION,"Add new location", R.drawable.ic_add_new_location))
        widgetList.add(WidgetModel(NavigationEnum.DAILY_CALL_RECORDING,"Daily Call Recording", R.drawable.ic_daily_call_recording))
        widgetList.add(WidgetModel(NavigationEnum.ATTENDANCE_REGISTER,"Attendance Register", R.drawable.ic_attendance_register))
        widgetList.add(WidgetModel(NavigationEnum.ADD_DOCTOR_PHARMACY,"Add Doctor or Pharmacy", R.drawable.ic_add_doctor_pharmacy))
        widgetList.add(WidgetModel(NavigationEnum.SEARCH_DOCTOR_PHARMACY,"Search Doctor or Pharmacy", R.drawable.ic_search_doctor_pharmacy))

        dashboardWidgetAdapter.setWidgetModelList(widgetList)
    }

    override fun subscribeObservers() {
        viewModel.userId.observe(viewLifecycleOwner,{
            userId = it
        })
        viewModel.markAttendanceResponse.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        if (it){
                            val intent = Intent(requireActivity(),CurrentActiveDayActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
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
    }

    private fun setupRvMenus(){
        dashboardWidgetAdapter = DashboardWidgetAdapter( this)
        binding.rvMenus.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = dashboardWidgetAdapter
        }
    }
/*

    override fun onMenuClick(menuId: Int) {
        when(menuId){
            0->{startActivity(Intent(requireActivity(), ManageSchedule::class.java))}
            1->{
                val sheet = AddNewArea()
                sheet.show(parentFragmentManager,sheet.tag)
            }
        }
    }
*/

    override fun onItemClick(item: WidgetModel) {
        when(item.id){
            NavigationEnum.MANAGE_SCHEDULE->{startActivity(Intent(requireActivity(), ManageSchedule::class.java))}
        }
    }

}