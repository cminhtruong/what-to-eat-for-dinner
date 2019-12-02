package com.eleven.ctruong.w2eat.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.eleven.ctruong.w2eat.R

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


        val navController = this.findNavController(R.id.loginNavHostFragment)

    }

}
