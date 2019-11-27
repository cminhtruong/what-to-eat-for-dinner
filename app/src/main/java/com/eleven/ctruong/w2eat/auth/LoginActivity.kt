package com.eleven.ctruong.w2eat.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eleven.ctruong.w2eat.R
import com.eleven.ctruong.w2eat.auth.ui.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }

}
