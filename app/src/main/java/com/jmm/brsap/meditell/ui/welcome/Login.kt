package com.jmm.brsap.meditell.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentLoginBinding
import com.jmm.brsap.meditell.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class Login : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val userName = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            lifecycleScope.launch {
                displayLoading(true)
                delay(2000)
                if (userName=="MTS10001" && password=="1234"){
                    startActivity(Intent(requireActivity(),MainDashboard::class.java))
                    requireActivity().finish()
                    showToast("Logged in successfully !!!")
                    displayLoading(false)
                }
                else{
                    displayLoading(false)
                    showToast("Incorrect Username or password !!!")
                }
            }

        }
    }
    override fun subscribeObservers() {

    }

}