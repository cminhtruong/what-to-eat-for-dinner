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

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eleven.ctruong.w2eat.helper.lengthGreaterThanEight
import com.eleven.ctruong.w2eat.helper.retryWhenError
import com.eleven.ctruong.w2eat.helper.verifyEmailPattern
import com.eleven.ctruong.w2eat.helper.verifyPasswordPattern
import com.eleven.ctruong.w2eat.repositories.local.AppDatabaseDao
import com.eleven.ctruong.w2eat.repositories.models.User
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
class SignUpViewModel(private val database: AppDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?>
        get() = _email

    private val _emailConfirm = MutableLiveData<String?>()
    val emailConfirm: LiveData<String?>
        get() = _emailConfirm

    private val _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password

    private val _passwordConfirm = MutableLiveData<String?>()
    val passwordConfirm: LiveData<String?>
        get() = _passwordConfirm

    private val _emailMessageError = object : MutableLiveData<String?>() {
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
                        { Timber.d("onComplete") })
            }
        }

    }
    val emailMessageError: LiveData<String?>
        get() = _emailMessageError

    private val _emailConfirmMessageError = object : MutableLiveData<String?>() {
        override fun onActive() {
            super.onActive()
            value?.let { return }
            uiScope.launch {
                delay(1000)
                Observable.just(_emailConfirm.value.toString())
                    .compose(verifyEmailPattern)
                    .compose(matchingConfirmField)
                    .compose(retryWhenError { value = it.message })
                    .subscribe(
                        { Timber.d("value: $it") },
                        { value = it.message },
                        { Timber.d("onComplete") })
            }
        }
    }
    val emailConfirmMessageError: LiveData<String?>
        get() = _emailConfirmMessageError

    private val _passwordMessageError = object : MutableLiveData<String?>() {
        override fun onActive() {
            super.onActive()
            value?.let { return }
            uiScope.launch {
                delay(1000)
                Observable.just(_password.value.toString())
                    .compose(verifyPasswordPattern)
                    .compose(lengthGreaterThanEight)
                    .compose(retryWhenError { value = it.message })
                    .subscribe(
                        { Timber.d("value: $it") },
                        { value = it.message },
                        { Timber.d("onComplete") }
                    )
            }
        }
    }
    val passwordMessageError: LiveData<String?>
        get() = _passwordMessageError

    private val _passwordConfirmMessageError = object : MutableLiveData<String?>() {
        override fun onActive() {
            super.onActive()
            value?.let { return }
            uiScope.launch {
                delay(1000)
                Observable.just(_passwordConfirm.value.toString())
                    .compose(verifyPasswordPattern)
                    .compose(lengthGreaterThanEight)
                    .compose(matchingConfirmField)
                    .compose(retryWhenError { value = it.message })
                    .subscribe(
                        { Timber.d("value: $it") },
                        { value = it.message },
                        { Timber.d("onComplete") }
                    )
            }
        }
    }
    val passwordConfirmMessageError: LiveData<String?>
        get() = _passwordConfirmMessageError

    private val _isUserCreated = MutableLiveData<Boolean>()
    val isUserCreated: LiveData<Boolean>
        get() = _isUserCreated

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _progressBarVisibility = MutableLiveData<Int>()
    val progressBarVisibility: LiveData<Int>
        get() = _progressBarVisibility

    init {
        _email.value = ""
        _emailConfirm.value = ""
        _emailMessageError.value = ""
        _emailConfirmMessageError.value = ""

        _password.value = ""
        _passwordConfirm.value = ""
        _passwordMessageError.value = ""
        _passwordConfirmMessageError.value = ""

        _isUserCreated.value = false
        _navigateToLogin.value = false
        _progressBarVisibility.value = 8
    }

    private fun createNewUser(email: String, password: String) {
        val user = User(0, email, password)
        return database.insertUser(user)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val matchingConfirmField = ObservableTransformer<String, String> { observable ->
        observable.map { it.trim() }
            .filter {
                it == _email.value || it == _password.value
            }
            .singleOrError()
            .onErrorResumeNext {
                when (it) {
                    is NoSuchElementException -> {
                        Single.error(Exception("Field doesn't matched"))
                    }
                    else -> {
                        Single.error(it)
                    }
                }
            }
            .toObservable()
    }


    fun onUserCreatedComplete() {
        _isUserCreated.value = true
    }

    fun onCancel() {
        _navigateToLogin.value = true
    }

    fun onCancelComplete() {
        _navigateToLogin.value = false
    }

    fun onSubmit() {
        _progressBarVisibility.value = View.VISIBLE
        when {
            !_email.value.isNullOrEmpty() && !_emailConfirm.value.isNullOrBlank() && !_password.value.isNullOrEmpty() && !_passwordConfirm.value.isNullOrBlank() -> {
                Timber.d("Add new user to system")
                onUserCreatedComplete()
                //createNewUser(_email.value!!, _password.value!!)
            }
            else -> {

            }
        }
    }
}
