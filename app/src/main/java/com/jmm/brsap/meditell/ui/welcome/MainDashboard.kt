package com.jmm.brsap.meditell.ui.welcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jmm.brsap.meditell.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dashboard)
    }
}