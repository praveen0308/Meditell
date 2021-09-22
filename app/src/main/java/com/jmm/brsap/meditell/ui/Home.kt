package com.jmm.brsap.meditell.ui

import android.os.Bundle
import android.view.View
import com.jmm.brsap.meditell.databinding.FragmentHomeBinding
import com.jmm.brsap.meditell.util.BaseFragment

class Home : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun subscribeObservers() {

    }

}