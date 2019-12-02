package com.eleven.ctruong.w2eat.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eleven.ctruong.w2eat.R
import com.eleven.ctruong.w2eat.auth.ui.login.LoginFragment

/**
 * The activity for Login module
 *
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
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
