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

package com.eleven.ctruong.w2eat.auth.ui.signup

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
import com.eleven.ctruong.w2eat.databinding.FragmentSignUpBinding
import com.eleven.ctruong.w2eat.repositories.local.AppDatabase
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

/**
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel
    private lateinit var viewModelFactory: SignUpViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")

        // Get reference to the binding object and inflate the fragment views
        val binding = DataBindingUtil.inflate<FragmentSignUpBinding>(
            inflater, R.layout.fragment_sign_up, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).appDatabaseDao
        viewModelFactory = SignUpViewModelFactory(dataSource)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SignUpViewModel::class.java)

        binding.signUpViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.emailMessageError.observe(
            this,
            Observer { errMessage -> binding.emailEtCreatedLayout.error = errMessage })

        viewModel.emailConfirmMessageError.observe(this, Observer { errMessage ->
            binding.emailEtConfirmLayout.error = errMessage
        })

        viewModel.passwordMessageError.observe(this, Observer { errMessage ->
            binding.passwordCreatedLayout.error = errMessage
        })

        viewModel.passwordConfirmMessageError.observe(this, Observer { errMessage ->
            binding.passwordConfirmLayout.error = errMessage
        })

        viewModel.isUserCreated.observe(this, Observer { hasCreated ->
            if (hasCreated) signUpFinished(viewModel.email.value!!)
        })
        return binding.root
    }

    private fun signUpFinished(email: String) {
        view?.let { Snackbar.make(it, "Welcome $email!", Snackbar.LENGTH_LONG).show() }
        val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
        findNavController().navigate(action)
        viewModel.onUserCreatedComplete()
    }
}