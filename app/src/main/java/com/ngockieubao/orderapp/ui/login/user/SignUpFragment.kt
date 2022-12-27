package com.ngockieubao.orderapp.ui.login.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.LoginViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentSignUpBinding
import com.ngockieubao.orderapp.ui.login.LoginViewModel

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding
        get() = _binding!!

    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var email: String? = null
        var passwd: String? = null
        var repasswd: String? = null

        binding.edtInputEmailSignUp.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    email = s.toString()
                } else
                    Toast.makeText(requireActivity(), "Email is not empty", Toast.LENGTH_SHORT).show()
            }
        })

        binding.edtInputPasswdSignUp.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    passwd = s.toString()
                } else
                    Toast.makeText(requireActivity(), "Password is not empty", Toast.LENGTH_SHORT).show()
            }
        })

        binding.edtInputConfirmPasswdSignUp.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    repasswd = s.toString()
                } else
                    Toast.makeText(requireActivity(), "Repassword is not empty", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnSignUpNow.setOnClickListener {
            if (repasswd == passwd) {
                loginViewModel.createAccount(email, passwd)
            } else {
                Toast.makeText(requireActivity(), "Confirm password wrong!", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.signUp.observe(this.viewLifecycleOwner) {
            if (it == null) return@observe
            else Log.d(TAG, "onCreateView: $it")
            Toast.makeText(requireActivity(), "Register success", Toast.LENGTH_SHORT).show()
            this.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.tvClickToLogin.setOnClickListener {
            this.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "SignUpFragment"
    }
}