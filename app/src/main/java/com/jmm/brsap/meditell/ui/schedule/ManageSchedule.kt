package com.jmm.brsap.meditell.ui.schedule

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.ScheduleAdapter
import com.jmm.brsap.meditell.databinding.ActivityManageScheduleBinding
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.BaseActivity
import com.jmm.brsap.meditell.util.MyDividerItemDecoration
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.ManageScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.security.AccessController.getContext
import javax.inject.Inject


@AndroidEntryPoint
class ManageSchedule :
    BaseActivity<ActivityManageScheduleBinding>(ActivityManageScheduleBinding::inflate){

    private val viewModel by viewModels<ManageScheduleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun subscribeObservers() {

    }

}