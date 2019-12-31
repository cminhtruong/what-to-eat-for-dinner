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

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eleven.ctruong.w2eat.helper.retryWhenError
import com.eleven.ctruong.w2eat.helper.verifyEmailPattern
import com.eleven.ctruong.w2eat.repositories.local.AppDatabaseDao
import io.reactivex.Observable
import kotlinx.coroutines.*
import timber.log.Timber

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
    val emailFP: LiveData<String?>
        get() = _email

    private val _emailFPMessageError = object : MutableLiveData<String?>() {
        override fun onActive() {
            super.onActive()
            value?.let { return }
            uiScope.launch {
                delay(1000)
                Observable.just(_email.value.toString())
                    .compose(verifyEmailPattern)
                    .compose(retryWhenError { value = it.message })
                    .subscribe(
                        { Timber.d("value: $it") },
                        { value = it.message },
                        { Timber.d("onComplete") }
                    )
            }
        }
    }
    val emailFBMessageError: LiveData<String?>
        get() = _emailFPMessageError

    private val _isRequestNewPassword = MutableLiveData<Boolean>()
    val isRequestNewPassword: LiveData<Boolean>
        get() = _isRequestNewPassword

    private val _progressBarFPVisibility = MutableLiveData<Int?>()
    val progressBarFBVisibility: LiveData<Int?>
        get() = _progressBarFPVisibility

    init {
        _email.value = ""
        _emailFPMessageError.value = ""
        _isRequestNewPassword.value = false
        _progressBarFPVisibility.value = 8
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onFBSubmitComplete() {
        _isRequestNewPassword.value = false
    }

    fun onFPSubmit() {
        _progressBarFPVisibility.value = View.VISIBLE
        when {
            _email.value!!.isNotEmpty() -> {
                Timber.d("Submit confirm")
                _isRequestNewPassword.value = true
            }
            else -> {
                _isRequestNewPassword.value = false
            }
        }
    }
}