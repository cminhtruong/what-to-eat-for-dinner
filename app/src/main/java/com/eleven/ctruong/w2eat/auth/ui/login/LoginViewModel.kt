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

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eleven.ctruong.w2eat.helper.lengthGreaterThanEight
import com.eleven.ctruong.w2eat.helper.retryWhenError
import com.eleven.ctruong.w2eat.helper.verifyEmailPattern
import com.eleven.ctruong.w2eat.helper.verifyPasswordPattern
import com.eleven.ctruong.w2eat.repositories.local.AppDatabaseDao
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
class LoginViewModel(private val database: AppDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _email = MutableLiveData<String?>()
    val emailLogin: LiveData<String?>
        get() = _email

    private val _emailMessageError = MutableLiveData<String?>()
    val emailMessageError: LiveData<String?>
        get() = _emailMessageError

    private val _password = MutableLiveData<String?>()
    val passwordLogin: LiveData<String?>
        get() = _password

    private val _passwordMessageError = MutableLiveData<String?>()
    val passwordMessageError: LiveData<String?>
        get() = _passwordMessageError

    private val _progressBarLoginVisibility = MutableLiveData<Int>()
    val progressBarLoginVisibility: LiveData<Int>
        get() = _progressBarLoginVisibility

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean>
        get() = _isLogin

    private val _navigateToForgotPassword = MutableLiveData<Boolean>()
    val navigateToForgotPassword: LiveData<Boolean>
        get() = _navigateToForgotPassword

    private val _navigateToSignUp = MutableLiveData<Boolean>()
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    init {
        Timber.d("init screen")
        _progressBarLoginVisibility.value = 8
        _navigateToSignUp.value = false
        _navigateToForgotPassword.value = false
        //_isLogin.value = false

        //onEmailChanged()
        //onPasswordChanged()
    }

    private fun onEmailChanged() {
        uiScope.launch {
            Observable.just(_email.value.toString())
                .compose(verifyEmailPattern)
                .compose(retryWhenError { _emailMessageError.value = it.message })
                .subscribe()
        }
    }

    private fun onPasswordChanged() {
        uiScope.launch {
            Observable.just(_password.value.toString())
                .compose(verifyPasswordPattern)
                .compose(lengthGreaterThanEight)
                .compose(retryWhenError { _passwordMessageError.value = it.message })
                .subscribe()
        }
    }

    fun onUserLogin() {
        _isLogin.value = true
        _progressBarLoginVisibility.value = View.VISIBLE
    }

    fun onUserLoginComplete() {
        _isLogin.value = false
        _progressBarLoginVisibility.value = View.GONE
    }

    fun navigateToForgotPasswordForm() {
        _navigateToForgotPassword.value = true
    }

    fun navigateToForgotPasswordFormComplete() {
        _navigateToForgotPassword.value = false
    }

    fun navigateToSignUpForm() {
        Timber.d("navigateToSignUpForm")
        _navigateToSignUp.value = true
    }

    fun navigateToSignUpFormComplete() {
        Timber.d("navigateToSignUpFormComplete")
        _navigateToSignUp.value = false
    }
}
