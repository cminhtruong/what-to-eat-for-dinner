package com.eleven.ctruong.w2eat.auth.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.eleven.ctruong.w2eat.R
import com.eleven.ctruong.w2eat.databinding.FragmentLoginBinding

/**
 * @author el_even
 * @version 1.0
 * @since 2019, Dec 2nd
 */
class LoginFragment : Fragment() {

    companion object {
        fun newInstance() =
            LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        return binding.root
    }
}
