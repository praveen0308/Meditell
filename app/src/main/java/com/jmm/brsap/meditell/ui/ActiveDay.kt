package com.jmm.brsap.meditell.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentActiveDayBinding
import com.jmm.brsap.meditell.util.BaseFragment

class ActiveDay : BaseFragment<FragmentActiveDayBinding>(FragmentActiveDayBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAddNewSchedule.setOnClickListener {

        }
    }

    override fun subscribeObservers() {

    }

}