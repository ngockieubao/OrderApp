package com.ngockieubao.orderapp.ui.login.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ngockieubao.orderapp.databinding.ActivityAdminBinding
import com.ngockieubao.orderapp.ui.login.user.SignOutDialog

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var dialog: SignOutDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = SignOutDialog()

        binding.constraintLayoutManageReceipt.setOnClickListener { }
        binding.constraintLayoutStatistic.setOnClickListener { }
        binding.constraintLayoutUpdateProduct.setOnClickListener { }
        binding.constraintLayoutSignOut.setOnClickListener {
            dialog.show(supportFragmentManager, "sign_out")
//            this.finish()
        }
    }
}