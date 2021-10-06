package com.jmm.brsap.meditell.ui.doctorpharmacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jmm.brsap.meditell.databinding.ActivitySearchDoctorPharmacyBinding
import com.jmm.brsap.meditell.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDoctorPharmacy : BaseActivity<ActivitySearchDoctorPharmacyBinding>(ActivitySearchDoctorPharmacyBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun subscribeObservers() {

    }
}