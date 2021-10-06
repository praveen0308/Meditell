package com.jmm.brsap.meditell.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentDailyCallRecordingBinding
import com.jmm.brsap.meditell.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyCallRecording : BaseFragment<FragmentDailyCallRecordingBinding>(FragmentDailyCallRecordingBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun subscribeObservers() {

    }

}