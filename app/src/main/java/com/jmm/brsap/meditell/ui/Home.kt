package com.jmm.brsap.meditell.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.type.DateTime
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.MenusAdapter
import com.jmm.brsap.meditell.databinding.FragmentHomeBinding
import com.jmm.brsap.meditell.model.ModelMenu
import com.jmm.brsap.meditell.ui.currentdayactivity.CurrentActiveDayActivity
import com.jmm.brsap.meditell.ui.schedule.ManageSchedule
import com.jmm.brsap.meditell.util.*

import com.jmm.brsap.meditell.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Time
import java.util.*

@AndroidEntryPoint
class Home : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    MenusAdapter.MenusInterface {

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var menusAdapter: MenusAdapter
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
            val intent = Intent(requireActivity(),CurrentActiveDayActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun populateMenus() {
        val menuList= mutableListOf<ModelMenu>()
        menuList.add(ModelMenu("Manage Schedule", R.drawable.ic_baseline_schedule_24))
        menuList.add(ModelMenu("Add new area", R.drawable.ic_baseline_schedule_24))
        menusAdapter.setModelMenuList(menuList)
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
        menusAdapter = MenusAdapter(this)
        binding.rvMenus.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = menusAdapter
        }
    }

    override fun onMenuClick(menuId: Int) {
        when(menuId){
            0->{startActivity(Intent(requireActivity(), ManageSchedule::class.java))}
            1->{
                val sheet = AddNewArea()
                sheet.show(parentFragmentManager,sheet.tag)
            }
        }
    }

}