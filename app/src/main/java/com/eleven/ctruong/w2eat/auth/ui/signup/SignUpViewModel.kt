package com.eleven.ctruong.w2eat.auth.ui.signup

import androidx.lifecycle.ViewModel
import com.eleven.ctruong.w2eat.repositories.local.AppDatabaseDao

/**
 * ViewModel for sign up fragment
 *
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
class SignUpViewModel(
    private val email: String,
    private val password: String,
    val database: AppDatabaseDao
) : ViewModel() {
}