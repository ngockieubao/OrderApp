package com.ngockieubao.orderapp.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            LoginViewModel.LoginViewModelFactory(requireActivity().application)
        )[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        var email: String? = null
        var passwd: String? = null

        binding.edtInputEmailLogin.doOnTextChanged { text, start, before, count ->
            if (text!!.length > 25) {
                binding.edtInputEmailLogin.error = "No more!"
            } else if (text.length < 25) {
                binding.edtInputEmailLogin.error = null
                Log.d(TAG, "onCreateView: $email")
            }
        }

        binding.edtInputPasswdLogin.doOnTextChanged { text, start, before, count ->
            if (text!!.length > 15) {
                binding.edtInputPasswdLogin.error = "Too long!"
            } else if (text.length < 15) {
                binding.edtInputPasswdLogin.error = null
                Log.d(TAG, "onCreateView: $passwd")
            }
        }

        binding.edtInputEmailLogin.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    email = s.toString()
                } else
                    Toast.makeText(requireActivity(), "Email is not empty", Toast.LENGTH_SHORT).show()
            }
        })

        binding.edtInputPasswdLogin.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    passwd = s.toString()
                } else
                    Toast.makeText(requireActivity(), "Password is not empty", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnLoginNow.setOnClickListener {
            loginViewModel.signIn(email, passwd)
        }

        loginViewModel.login.observe(this.viewLifecycleOwner) {
            if (it == null) return@observe
            else Log.d(TAG, "onCreateView: $it")
            Toast.makeText(requireActivity(), "Login success", Toast.LENGTH_SHORT).show()
            this.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvClickToSignUp.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}