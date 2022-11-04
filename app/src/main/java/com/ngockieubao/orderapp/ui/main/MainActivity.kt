package com.ngockieubao.orderapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.ActivityMainBinding
import com.ngockieubao.orderapp.ui.login.SignOutDialog


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var mOrderViewModel: OrderViewModel
    private lateinit var dialog: SignOutDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        mOrderViewModel = ViewModelProvider(this@MainActivity, OrderViewModelFactory(application))[OrderViewModel::class.java]

        dialog = SignOutDialog()
        binding.imgvHeaderUser.setOnClickListener {
            dialog.show(supportFragmentManager, "sign_out")
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
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragNavHost)
        return navController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}