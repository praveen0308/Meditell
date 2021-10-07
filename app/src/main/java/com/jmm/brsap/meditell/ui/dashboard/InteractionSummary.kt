package com.jmm.brsap.meditell.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentInteractionSummaryBinding
import com.jmm.brsap.meditell.model.Doctor
import com.jmm.brsap.meditell.model.Pharmacy
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.DailyCallRecordingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InteractionSummary : BaseFragment<FragmentInteractionSummaryBinding>(FragmentInteractionSummaryBinding::inflate) {

    private val viewModel by activityViewModels<DailyCallRecordingViewModel>()

    private val navArgs by navArgs<InteractionSummaryArgs>()
    private var interactionId = 0
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainDashboard).setToolbarTitle("Summary")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interactionId=navArgs.interactionId
        viewModel.getInteractionSummary(interactionId)
    }

    override fun subscribeObservers() {
        viewModel.interactionSummary.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        binding.apply {
                            if (it.interactionWasWith=="doctor"){
                                tvInteractionWith.text = "Doctor : ${(it.interactedWithModel as Doctor).address.toString()}"
                            }else{
                                tvInteractionWith.text = "Pharmacy : ${(it.interactedWithModel as Pharmacy).address.toString()}"
                            }
                            tvInteractionType.text = "It was a ${it.type.toString().uppercase()}"
                            tvSummary.text = it.summary.toString()
                            Glide.with(requireContext())
                                .load(it.imageUrl)
                                .centerCrop()
                                .placeholder(R.drawable.ic_upload_picture)
                                .into(ivImageUploaded)


                        }
                    }
                    displayLoading(false)
                }
                Status.LOADING -> {
                    displayLoading(true)
                }
                Status.ERROR -> {
                    displayLoading(false)
                    _result.message?.let {
                        displayError(it)
                    }
                }
            }
        })
    }

}