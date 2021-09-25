package com.jmm.brsap.meditell.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentLoginBinding
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.util.Status
import com.jmm.brsap.meditell.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class Login : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()
    private var userName = ""
    private var password = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            userName = binding.etUsername.text.toString().trim()
            password = binding.etPassword.text.toString().trim()
            viewModel.checkAccountAlreadyExist(userName)

        }
    }
    override fun subscribeObservers() {
        viewModel.isUserExists.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        if (it){
                            viewModel.doLogin(userName, password)
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

        viewModel.loginResponse.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        try {
                            viewModel.updateWelcomeStatus(1)
                            viewModel.updateFirstName(it.firstName)
                            viewModel.updateLastName(it.lastName)
                            viewModel.updateUserName(it.userName)
                        }finally {
                            showToast("Login successful !!!")
                            startActivity(Intent(requireActivity(),MainDashboard::class.java))
                            requireActivity().finish()
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