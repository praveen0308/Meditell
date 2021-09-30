package com.jmm.brsap.meditell.ui.currentdayactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.CurrentDateAreaVisitAdapter
import com.jmm.brsap.meditell.adapters.MenusAdapter
import com.jmm.brsap.meditell.databinding.FragmentActiveDayBinding
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.convertDMY2EMDY
import com.jmm.brsap.meditell.util.getTodayDate
import com.jmm.brsap.meditell.viewmodel.CurrentDayActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActiveDay : BaseFragment<FragmentActiveDayBinding>(FragmentActiveDayBinding::inflate),
    CurrentDateAreaVisitAdapter.CurrentDateAreaVisitInterface {

    private val viewModel by activityViewModels<CurrentDayActivityViewModel>()
    private lateinit var currentDateAreaVisitAdapter: CurrentDateAreaVisitAdapter
    private var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvAreas()
        binding.tvTime.text = convertDMY2EMDY(getTodayDate())

        binding.fabAddNewSchedule.setOnClickListener {

        }
    }

    override fun subscribeObservers() {
        viewModel.userId.observe(viewLifecycleOwner,{
            userId = it
        })


    }
    private fun setupRvAreas(){
        currentDateAreaVisitAdapter = CurrentDateAreaVisitAdapter(this)
        binding.rvAreas.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(context,
                layoutManager.orientation)
            addItemDecoration(dividerItemDecoration)

            this.layoutManager = layoutManager
            adapter = currentDateAreaVisitAdapter
        }
    }

    override fun onItemClick(item: Area) {

    }
}