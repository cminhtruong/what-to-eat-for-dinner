package com.eleven.ctruong.w2eat.auth.ui.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.eleven.ctruong.w2eat.R

/**
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
class ForgotPasswordFragment : Fragment() {

    companion object {
        fun newInstance(): ForgotPasswordFragment = ForgotPasswordFragment()
    }

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_forgot_password, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)
    }
}