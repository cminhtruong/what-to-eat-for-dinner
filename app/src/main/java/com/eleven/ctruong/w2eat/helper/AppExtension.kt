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
package com.eleven.ctruong.w2eat.helper

import android.util.Patterns
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single

val PATTERN_PASSWORD =
    """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{8,}""".toRegex()

val lengthGreaterThanEight = ObservableTransformer<String, String> { observable ->
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

val verifyEmailPattern = ObservableTransformer<String, String> { observable ->
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

val verifyPasswordPattern = ObservableTransformer<String, String> { observable ->
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

inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit): ObservableTransformer<String, String> =
    ObservableTransformer { observable ->
        observable.retryWhen { errors ->
            errors.flatMap {
                onError(it)
                Observable.just("")
            }
        }
    }