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

import android.util.Patterns
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.eleven.ctruong.w2eat.repositories.local.AppDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.regex.Pattern

/**
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
class ForgotPasswordViewModel(private val database: AppDatabaseDao) :
    ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _email = MutableLiveData<String?>()
    val emailFP: MutableLiveData<String?>
        get() = _email

    private val _isEmailValid: LiveData<Boolean> = Transformations.map(_email) { stream ->
        stream!!.isNotEmpty() && Pattern.matches(Patterns.EMAIL_ADDRESS.toString(), stream)
    }

    val emailFPMessageError: LiveData<String?>
        get() = Transformations.map(_isEmailValid) { isValid ->
            if (isValid) {
                ""
            } else {
                "Password not valid"
            }
        }

    private val _isRequestNewPassword = MutableLiveData<Boolean>()
    val isRequestNewPassword: LiveData<Boolean>
        get() = _isRequestNewPassword

    private val _progressBarFPVisibility = MutableLiveData<Int?>()
    val progressBarFBVisibility: MutableLiveData<Int?>
        get() = _progressBarFPVisibility

    init {

        _isRequestNewPassword.value = false

        _progressBarFPVisibility.value = 8
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onFBSubmitComplete() {
        _isRequestNewPassword.value = false
        _progressBarFPVisibility.value = 8
    }

    fun onFPSubmit() {
        _isRequestNewPassword.value = true
        _progressBarFPVisibility.value = View.VISIBLE
//        Timber.d("Submit confirm ${_email.value}")

//        when {
//            _email.value!!.isNotEmpty() -> {
//
//                _isRequestNewPassword.value = true
//            }
//            else -> {
//                _isRequestNewPassword.value = false
//            }
//        }
    }
}