package com.jmm.brsap.meditell.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentSplashScreenBinding
import com.jmm.brsap.meditell.repository.UserPreferencesRepository.Companion.LOGIN_DONE
import com.jmm.brsap.meditell.ui.currentdayactivity.CurrentActiveDayActivity
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.util.getTodayDate
import com.jmm.brsap.meditell.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import timber.log.Timber


@AndroidEntryPoint
class SplashScreen : BaseFragment<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    private var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            performNavigation()
        }, 2000)


    }

    private fun performNavigation() {
        viewModel.userId.observe(viewLifecycleOwner,{
            userId = it
        })
        viewModel.welcomeStatus.observe(viewLifecycleOwner,{
            if (it==LOGIN_DONE){
                Timber.d(getTodayDate())
                viewModel.getDayStatus(userId, getTodayDate())
                /*findNavController().navigate(SplashScreenDirections.actionSplashScreenToMainDashboard())
                requireActivity().finish()*/
            }else{
                findNavController().navigate(SplashScreenDirections.actionSplashScreenToLogin())
            }
        })

        viewModel.dayStatus.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        when(it){
                            0->{
                                findNavController().navigate(SplashScreenDirections.actionSplashScreenToMainDashboard())
                                requireActivity().finish()
                            }
                            1->{
                                findNavController().navigate(SplashScreenDirections.actionSplashScreenToCurrentActiveDayActivity())
                                requireActivity().finish()
                            }
                            2->{
                                findNavController().navigate(SplashScreenDirections.actionSplashScreenToMainDashboard())
                                requireActivity().finish()
                            }
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

    override fun subscribeObservers() {

    }

}