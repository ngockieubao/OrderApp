package com.ngockieubao.orderapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtInputEmailLogin.doOnTextChanged { text, start, before, count ->
            if (text!!.length > 25) {
                binding.edtInputEmailLogin.error = "No more!"
            } else if (text.length < 25) {
                binding.edtInputEmailLogin.error = null
            }
        }

        binding.tvClickToSignUp.setOnClickListener {
            this.findNavController().navigate(R.id.signUpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}