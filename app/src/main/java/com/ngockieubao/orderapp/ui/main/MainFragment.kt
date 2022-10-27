package com.ngockieubao.orderapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.databinding.FragmentMainBinding
import com.ngockieubao.orderapp.ui.login.SignOutDialog

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var dialog: SignOutDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        dialog = SignOutDialog()
        binding.imgvHeaderUser.setOnClickListener {
            dialog.show(parentFragmentManager, "sign_out")
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.orderFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.confirmOrderFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.cartFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                }
                else -> {
                    binding.constraintHeader.visibility = View.VISIBLE
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}