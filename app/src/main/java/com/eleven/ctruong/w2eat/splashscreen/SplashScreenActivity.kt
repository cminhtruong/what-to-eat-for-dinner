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

package com.eleven.ctruong.w2eat.splashscreen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eleven.ctruong.w2eat.R
import com.eleven.ctruong.w2eat.auth.LoginActivity
import com.eleven.ctruong.w2eat.main.MainActivity
import com.eleven.ctruong.w2eat.repositories.models.User
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Splash screen module
 *
 * @author el_even
 * @version 1.0
 * @since 2019, Nov 27th
 */
class SplashScreenActivity : AppCompatActivity(), CoroutineScope {

    private val _sharedPref: SharedPreferences
        get() = getPreferences(Context.MODE_PRIVATE)

    private val _isPreferencesAvailable: Boolean
        get() = _sharedPref.checkIfPreferencesAvailable()

    private val _loginSession: String?
        get() = _sharedPref.getString(getString(R.string.user_session), "")

    private val _passwordSession: String?
        get() = _sharedPref.getString(getString(R.string.password_session), "")

    private val _isSessionExist: Boolean
        get() = (_loginSession!!.checkIfUserSessionExist() && _passwordSession!!.checkIfUserSessionExist())

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Timber.d("onCreate")

        launch {
            delay(5000)
            if (_isPreferencesAvailable && _isSessionExist) {
                Timber.d("User $_loginSession exists in cache")
                val user = User(0, _loginSession!!, _passwordSession!!)
                // TODO 1: required connection to server Firebase to retrieve user's data in Json
                startActivity(Intent(applicationContext, MainActivity::class.java).apply {
                    putExtra("user", user.toString())
                })
            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
    }

    private fun SharedPreferences.checkIfPreferencesAvailable(): Boolean =
        this.contains(getString(R.string.user_session)) && this.contains(getString(R.string.password_session))

    private fun String.checkIfUserSessionExist(): Boolean = isNotBlank()

}
