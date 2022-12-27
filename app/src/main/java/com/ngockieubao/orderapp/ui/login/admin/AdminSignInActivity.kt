package com.ngockieubao.orderapp.ui.login.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.ActivityAdminSignInBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel

class AdminSignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedViewModel: OrderViewModel by viewModels() {
            OrderViewModelFactory(this.application)
        }

        var email: String? = null
        var passwd: String? = null

        binding.edtInputAdminLoginName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length > 25) {
                    binding.edtInputAdminLoginName.error = "No more!"
                } else if (s.length < 25) {
                    binding.edtInputAdminLoginName.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    email = s.toString()
                }
//                else Toast.makeText(this, "Email is not empty", Toast.LENGTH_SHORT).show()
            }
        })
        binding.edtInputAdminLoginPasswd.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length > 15) {
                    binding.edtInputAdminLoginPasswd.error = "Too long!"
                } else if (s.length < 15) {
                    binding.edtInputAdminLoginPasswd.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    passwd = s.toString()
                }
//                else Toast.makeText(this, "Password is not empty", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnLoginNow.setOnClickListener {
            if (email == null || passwd == null) {
                Toast.makeText(this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show()
            } else {
                sharedViewModel.signInAdmin(email!!, passwd!!)

                sharedViewModel.admin.observe(this) {
                    if (it == null) return@observe
                    else {
                        Log.d("AdminLoginFragment.TAG", "onCreateView: $it")
                        Toast.makeText(this, "Welcome, admin!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AdminActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}