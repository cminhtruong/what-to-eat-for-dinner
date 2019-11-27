package com.eleven.ctruong.w2eat.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eleven.ctruong.w2eat.R
import com.eleven.ctruong.w2eat.main.ui.MainFragment

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
