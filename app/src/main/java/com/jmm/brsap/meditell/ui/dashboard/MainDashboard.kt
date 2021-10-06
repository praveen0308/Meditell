package com.jmm.brsap.meditell.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.ActivityMainDashboardBinding
import com.jmm.brsap.meditell.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainDashboard : BaseActivity<ActivityMainDashboardBinding>(ActivityMainDashboardBinding::inflate) {
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onBackPressed() {

        if(doubleBackToExitPressedOnce) {
            super.onBackPressed()
        }
        else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    fun setToolbarTitle(title:String){
        binding.toolbarMainDashboard.setTitle(title)
    }
    override fun subscribeObservers() {

    }


}