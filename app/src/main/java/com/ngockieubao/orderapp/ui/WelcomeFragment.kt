package com.ngockieubao.orderapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.IOnBackPressed
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.LoginViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentWelcomeBinding
import com.ngockieubao.orderapp.ui.login.LoginViewModel

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(requireActivity().application)
    }
    private val oneTapResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            loginViewModel.initOneTapResult(result, requireActivity())
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = loginViewModel.checkCurrentUser()
        if (currentUser == null) return
        else {
            this.findNavController().navigate(R.id.action_welcomeFragment_to_mainActivity)
            Toast.makeText(requireActivity(), "${currentUser.email}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
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
        binding.btnOneTap.setOnClickListener {
            loginViewModel.initParams(requireActivity())
            loginViewModel.displaySignIn(
                requireActivity(),
                loginViewModel.oneTapClient,
                loginViewModel.signInRequest,
                loginViewModel.signUpRequest,
                oneTapResult
            )
        }
        binding.textViewLoginForAdmin.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToAdminLoginFragment()
            this.findNavController().navigate(action)
        }

        loginViewModel.hasGoogleSignIn.observe(this.viewLifecycleOwner) {
            if (it == false) return@observe
            if (it == true) {
                this.findNavController().navigate(R.id.action_welcomeFragment_to_mainActivity)
            }
            loginViewModel.resetSignInGoogle()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "WelcomeFragment"
    }

//    override fun onBackPressed(): Boolean {
//        if (loginViewModel.checkCurrentUser() != null) {
//            //action not popBackStack
//            this.findNavController().navigate(R.id.action_welcomeFragment_to_mainActivity)
//            return true
//        } else {
//            return false
//        }
//    }
}