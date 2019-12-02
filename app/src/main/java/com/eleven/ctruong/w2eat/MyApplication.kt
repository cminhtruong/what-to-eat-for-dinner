package com.eleven.ctruong.w2eat

import android.app.Application
import timber.log.Timber

/**
 *
 * @author el_even
 * @version 1.0
 * @since 2019, Nov 27th
 */
class MyApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}