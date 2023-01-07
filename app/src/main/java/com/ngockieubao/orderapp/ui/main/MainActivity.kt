package com.ngockieubao.orderapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.LoginViewModelFactory
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.ActivityMainBinding
import com.ngockieubao.orderapp.ui.login.LoginViewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mOrderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory(application))[LoginViewModel::class.java]

        mOrderViewModel = ViewModelProvider(
            this@MainActivity,
            OrderViewModelFactory(application)
        )[OrderViewModel::class.java]
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.orderFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.categoryFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.confirmOrderFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                }
                R.id.cartFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                }
                R.id.profileFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                }
                R.id.receiptFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                }
                R.id.receiptDetailFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                }
                R.id.welcomeFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.homeAdminFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.adminLoginFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.manageInvoiceFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.manageProductFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.statisticFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.editProfileBtmFragment -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.invoiceDetailBtmSheet -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.mgrProductDetail -> {
                    binding.constraintHeader.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragNavHost)
        return navController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkUser() {
        if (loginViewModel.checkCurrentUser() == null) {
            Toast.makeText(this, "No user active", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "User ${loginViewModel.checkCurrentUser()!!.email} is active",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}