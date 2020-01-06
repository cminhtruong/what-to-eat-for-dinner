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

package com.eleven.ctruong.w2eat.auth.ui.forgot

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
import com.eleven.ctruong.w2eat.databinding.FragmentForgotPasswordBinding
import com.eleven.ctruong.w2eat.repositories.local.AppDatabase
import timber.log.Timber

/**
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
class ForgotPasswordFragment : Fragment() {

    companion object {
        fun newInstance(): ForgotPasswordFragment = ForgotPasswordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")
        val binding = DataBindingUtil.inflate<FragmentForgotPasswordBinding>(
            inflater,
            R.layout.fragment_forgot_password,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).appDatabaseDao
        val viewModelFactory = ForgotPasswordViewModelFactory(dataSource)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ForgotPasswordViewModel::class.java)
        binding.forgotPasswordViewModel = viewModel
        binding.lifecycleOwner = this
        setupObserver(viewModel)
        return binding.root
    }

    private fun setupObserver(
        vm: ForgotPasswordViewModel
    ) {
//        binding.emailForgot.afterTextChangeEvents().skipInitialValue()
//            .map {
//                binding.emailForgotLayout.error = null
//                it.view.text.toString()
//            }.debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
//            .compose(verifyEmailPattern)
//            .compose(retryWhenError { error -> binding.emailForgotLayout.error = error.message })
//            .subscribe()

        vm.isRequestNewPassword.observe(this, Observer { isRequested ->
            Timber.d("quest: $isRequested")
            when {
                isRequested -> {
//                    view?.let {
//                        Snackbar.make(it, "Please check your email", Snackbar.LENGTH_LONG).show()
//                    }
                    findNavController()
                        .navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment())
                    vm.onFBSubmitComplete()
                }
            }
        })

    }

}