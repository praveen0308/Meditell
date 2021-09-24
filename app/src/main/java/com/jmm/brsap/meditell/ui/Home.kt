package com.jmm.brsap.meditell.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.MenusAdapter
import com.jmm.brsap.meditell.databinding.FragmentHomeBinding
import com.jmm.brsap.meditell.model.ModelMenu
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.convertYMD2EMDY
import com.jmm.brsap.meditell.util.getTodayDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    MenusAdapter.MenusInterface {

    private lateinit var menusAdapter: MenusAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvMenus()
        populateMenus()
        binding.tvTime.text = convertYMD2EMDY(getTodayDate())

        binding.btnStartDay.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_activeDay)
        }
    }

    private fun populateMenus() {
        val menuList= mutableListOf<ModelMenu>()
        menuList.add(ModelMenu("Manage Schedule", R.drawable.ic_baseline_schedule_24))
        menusAdapter.setModelMenuList(menuList)
    }

    override fun subscribeObservers() {

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
            0->{startActivity(Intent(requireActivity(),ManageSchedule::class.java))}
        }
    }

}