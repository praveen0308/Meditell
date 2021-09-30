package com.jmm.brsap.meditell.ui.currentdayactivity

import android.os.Bundle
import android.view.View
import com.jmm.brsap.meditell.databinding.FragmentSubmitInteractionBinding
import com.jmm.brsap.meditell.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SubmitInteraction : BaseFragment<FragmentSubmitInteractionBinding>(FragmentSubmitInteractionBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun subscribeObservers() {

    }

}