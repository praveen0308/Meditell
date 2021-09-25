package com.jmm.brsap.meditell.ui.welcome

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
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class SplashScreen : BaseFragment<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            performNavigation()
        }, 2000)


    }

    private fun performNavigation() {
        viewModel.welcomeStatus.observe(viewLifecycleOwner,{
            if (it==LOGIN_DONE){
                findNavController().navigate(SplashScreenDirections.actionSplashScreenToMainDashboard())
            }else{
                findNavController().navigate(SplashScreenDirections.actionSplashScreenToLogin())
            }
        })

    }

    override fun subscribeObservers() {

    }

}