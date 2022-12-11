package com.ngockieubao.orderapp.ui.login.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ngockieubao.orderapp.IOnBackPressed
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.LoginViewModelFactory
import com.ngockieubao.orderapp.databinding.ActivityLoginBinding
import com.ngockieubao.orderapp.ui.login.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory(application))[LoginViewModel::class.java]
        checkUser()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.loginActNavHost)
        return navController.navigateUp()
    }

    private fun checkUser() {
        if (loginViewModel.checkCurrentUser() == null) {
            Toast.makeText(this, "LoginActivity No user active", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "LoginActivity has user active", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//
//        val fragment =
//            this.supportFragmentManager.findFragmentById(R.id.loginFragment)
//        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
//            super.onBackPressed()
//        }
//
//        if (loginViewModel.checkCurrentUser() == null) {
//            Toast.makeText(this, "LoginActivity has no user active", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "LoginActivity has user active", Toast.LENGTH_SHORT).show()
//        }
//    }
}