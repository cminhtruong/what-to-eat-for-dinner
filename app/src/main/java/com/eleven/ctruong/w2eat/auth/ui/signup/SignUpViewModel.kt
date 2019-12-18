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

    fun createNewUser(email: String, password: String) {
        val user = User(0, email, password)
        return database.insertUser(user)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onEmailChange() {
    }

    private val lengthGreaterThan8 = ObservableTransformer<String, String> { observable ->
        observable.flatMap { text ->
            Observable.just(text).map { it.trim() }
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
    }

    private val verifyEmailPattern = ObservableTransformer<String, String> { observable ->
        observable.flatMap { text ->
            Observable.just(text).map { it.trim() }
                .filter {
                    Patterns.EMAIL_ADDRESS.matcher(it).matches()
                }
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

}