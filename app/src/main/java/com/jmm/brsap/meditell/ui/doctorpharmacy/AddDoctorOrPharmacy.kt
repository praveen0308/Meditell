package com.jmm.brsap.meditell.ui.doctorpharmacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jmm.brsap.meditell.databinding.ActivityAddDoctorOrPharmacyBinding
import com.jmm.brsap.meditell.util.BaseActivity
import com.jmm.brsap.meditell.viewmodel.AddDoctorOrPharmacyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDoctorOrPharmacy : BaseActivity<ActivityAddDoctorOrPharmacyBinding>(ActivityAddDoctorOrPharmacyBinding::inflate) {

    private val viewModel by viewModels<AddDoctorOrPharmacyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewPager()

        binding.apply {
         /*   btnDoctor.setOnClickListener {
                viewModel.activeTab.postValue(0)
            }
            btnPharmacy.setOnClickListener {
                viewModel.activeTab.postValue(1)
            }*/
        }
    }

    override fun subscribeObservers() {
        viewModel.activeTab.observe(this,{
            binding.vpAddDoctorPharmacy.currentItem = it
           /* when(it){
                0->{
                    binding.apply {

                    }
                }
                1->{

                }
            }*/
        })
    }

    private fun setupViewPager() {
        binding.apply {

            vpAddDoctorPharmacy.adapter = MyVPAdapter(this@AddDoctorOrPharmacy)
            val tabLayoutMediator = TabLayoutMediator(
                tabLayout, vpAddDoctorPharmacy
            ) { tab, position ->
                when (position) {
                    0 -> tab.text = "Doctor"
                    1 -> tab.text = "Pharmacy"
                }
            }
            tabLayoutMediator.attach()
            vpAddDoctorPharmacy.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            })

        }

    }


    inner class MyVPAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AddNewDoctor()
                1 -> AddNewPharmacy()
                else -> AddNewDoctor()

            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }



}