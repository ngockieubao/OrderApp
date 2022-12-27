package com.ngockieubao.orderapp.ui.login.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.AdminViewModelFactory
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.ActivityAdminBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var mOrderViewModel: OrderViewModel
    private lateinit var mAdminViewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mOrderViewModel = ViewModelProvider(
            this@AdminActivity,
            OrderViewModelFactory(application)
        )[OrderViewModel::class.java]

        mAdminViewModel = ViewModelProvider(
            this@AdminActivity,
            AdminViewModelFactory(application)
        )[AdminViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.adminFragNavHost)
        return navController.navigateUp()
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//
//        mOrderViewModel.hasAdmin.observe(this) {
//            if (it != null) return@observe
//            else {
//                this.finish()
//            }
//        }
//    }
}