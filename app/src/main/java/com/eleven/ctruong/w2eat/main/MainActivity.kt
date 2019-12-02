package com.eleven.ctruong.w2eat.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eleven.ctruong.w2eat.R
import com.eleven.ctruong.w2eat.main.ui.MainFragment

/**
 * The main activity for main module
 *
 * @author El_even
 * @version 1.0
 * @since 2019, Nov 28th
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
