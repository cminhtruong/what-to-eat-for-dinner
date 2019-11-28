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
                val user = User(_loginSession!!, _passwordSession!!)
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
