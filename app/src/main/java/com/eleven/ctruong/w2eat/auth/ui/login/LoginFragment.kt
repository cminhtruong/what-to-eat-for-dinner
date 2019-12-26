/*
 * Copyright 2019, El_even
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eleven.ctruong.w2eat.auth.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.eleven.ctruong.w2eat.R
import com.eleven.ctruong.w2eat.databinding.FragmentLoginBinding
import com.eleven.ctruong.w2eat.repositories.local.AppDatabase

/**
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
class LoginFragment : Fragment() {

    companion object {
        fun newInstance() =
            LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).appDatabaseDao

        viewModelFactory = LoginViewModelFactory(dataSource)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.isFPRequest.observe(
            this,
            Observer { hasRequest -> if (hasRequest) openForgotPasswordForm() })
        viewModel.isNewAccountRequest.observe(
            this, Observer { hasRequest -> if (hasRequest) openSignUpForm() })
        viewModel.emailMessageError.observe(
            this,
            Observer { errMessage -> binding.emailEtLayout.error = errMessage })
        viewModel.passwordMessageError.observe(
            this,
            Observer { errMessage -> binding.passwordEtLayout.error = errMessage })

        return binding.root
    }

    private fun openSignUpForm() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        findNavController().navigate(action)
    }

    private fun openForgotPasswordForm() {
        val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
        findNavController().navigate(action)
    }
}
