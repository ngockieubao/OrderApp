package com.ngockieubao.orderapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.databinding.FragmentWelcomeBinding
import com.ngockieubao.orderapp.ui.login.LoginViewModel

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding
        get() = _binding!!

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            LoginViewModel.LoginViewModelFactory(requireActivity().application)
        )[LoginViewModel::class.java]
    }

    // Init auth
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            this.findNavController().navigate(R.id.loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            this.findNavController().navigate(R.id.signUpFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) return
        else {
            this.findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
            Toast.makeText(requireActivity(), "${currentUser.email} signed in", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}