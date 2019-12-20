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

import android.util.Patterns
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eleven.ctruong.w2eat.repositories.local.AppDatabaseDao
import com.eleven.ctruong.w2eat.repositories.models.User
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
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
class SignUpViewModel(val database: AppDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    companion object {
        private val PATTERN_PASSWORD =
            """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{8,}""".toRegex()
    }

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

    private val _emailMessageError = MutableLiveData<String?>()
    val emailMessageError: LiveData<String?>
        get() = _emailMessageError

    private val _emailConfirmMessageError = MutableLiveData<String?>()
    val emailConfirmMessageError: LiveData<String?>
        get() = _emailConfirmMessageError

    private val _passwordMessageError = MutableLiveData<String?>()
    val passwordMessageError: LiveData<String?>
        get() = _passwordMessageError

    private val _passwordConfirmMessageError = MutableLiveData<String?>()
    val passwordConfirmMessageError: LiveData<String?>
        get() = _passwordConfirmMessageError

    private val _isUserCreated = MutableLiveData<Boolean?>()
    val isUserCreated: LiveData<Boolean?>
        get() = _isUserCreated

    private val _progressBarVisibility = MutableLiveData<Int>()
    val progressBarVisibility: LiveData<Int>
        get() = _progressBarVisibility

    fun createNewUser(email: String, password: String) {
        val user = User(0, email, password)
        return database.insertUser(user)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onEmailChanged() {
        uiScope.launch {
            Observable.just(_email.value.toString())
                .compose(verifyEmailPattern)
                .compose(retryWhenError { _emailMessageError.value = it.message })
                .subscribe()
        }
    }

    fun onEmailConfirmChange() {
        uiScope.launch {
            Observable.just(_emailConfirm.value.toString())
                .compose(verifyEmailPattern)
                .compose(matchingConfirmField)
                .compose(retryWhenError { _emailConfirmMessageError.value = it.message })
                .subscribe()
        }
    }

    fun onPasswordChanged() {
        uiScope.launch {
            Observable.just(_password.value.toString())
                .compose(verifyPasswordPattern)
                .compose(lengthGreaterThanEight)
                .compose(retryWhenError { _passwordMessageError.value = it.message })
                .subscribe()
        }
    }

    fun onPasswordConfirmChanged() {
        uiScope.launch {
            Observable.just(_passwordConfirm.value.toString())
                .compose(verifyPasswordPattern)
                .compose(lengthGreaterThanEight)
                .compose(matchingConfirmField)
                .compose(retryWhenError { _passwordConfirmMessageError.value = it.message })
                .subscribe()
        }
    }

    private val matchingConfirmField = ObservableTransformer<String, String> { observable ->
        observable.map { it.trim() }
            .filter {
                it == _email.value || it == _password.value
            }.singleOrError().onErrorResumeNext {
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

    private val lengthGreaterThanEight = ObservableTransformer<String, String> { observable ->
        observable.map { it.trim() }
            .filter { it.length > 8 }
            .singleOrError()
            .onErrorResumeNext {
                when (it) {
                    is NoSuchElementException -> {
                        Single.error(Exception("Length should be greater than 6"))
                    }
                    else -> {
                        Single.error(it)
                    }
                }
            }
            .toObservable()

    }

    private val verifyEmailPattern = ObservableTransformer<String, String> { observable ->
        observable.map { it.trim() }
            .filter { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
            .singleOrError()
            .onErrorResumeNext {
                when (it) {
                    is NoSuchElementException -> {
                        Single.error(Exception("Email not valid"))
                    }
                    else -> {
                        Single.error(it)
                    }
                }
            }
            .toObservable()
    }

    private val verifyPasswordPattern = ObservableTransformer<String, String> { observable ->
        observable.flatMap { text ->
            Observable.just(text).map { it.trim() }
                .filter {
                    PATTERN_PASSWORD.matches(it)
                }
                .singleOrError()
                .onErrorResumeNext {
                    when (it) {
                        is NoSuchElementException -> {
                            Single.error(Exception("Password not valid"))
                        }
                        else -> {
                            Single.error(it)
                        }
                    }
                }
                .toObservable()
        }
    }

    private inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit): ObservableTransformer<String, String> =
        ObservableTransformer { observable ->
            observable.retryWhen { errors ->
                errors.flatMap {
                    onError(it)
                    Observable.just("")
                }
            }
        }

    fun onUserCreatedComplete() {
        _isUserCreated.value = true
    }

    fun onCancel() {
        _isUserCreated.value = false
    }

    fun onSubmit() {
        _progressBarVisibility.value = View.VISIBLE
        when {
            !_email.value.isNullOrEmpty() && !_emailConfirm.value.isNullOrBlank() && !_password.value.isNullOrEmpty() && !_passwordConfirm.value.isNullOrBlank() -> {
                Timber.d("Add new user to system")
                //createNewUser(_email.value!!, _password.value!!)
            }
            else -> {

            }
        }
    }
}
