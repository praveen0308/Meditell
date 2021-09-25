package com.jmm.brsap.meditell.ui.schedule

import android.os.Bundle
import com.jmm.brsap.meditell.databinding.ActivityAddScheduleBinding
import com.jmm.brsap.meditell.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSchedule : BaseActivity<ActivityAddScheduleBinding>(ActivityAddScheduleBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun subscribeObservers() {

    }
}