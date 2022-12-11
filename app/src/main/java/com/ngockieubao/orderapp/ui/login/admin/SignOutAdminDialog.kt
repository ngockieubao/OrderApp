package com.ngockieubao.orderapp.ui.login.admin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.ui.login.user.LoginActivity
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import java.lang.IllegalStateException

class SignOutAdminDialog : DialogFragment() {

    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure to log out?")
                .setPositiveButton("Yes") { dialog, id ->
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(requireActivity(), "Signed out", Toast.LENGTH_SHORT).show()
//                    it.supportFragmentManager.clearBackStack("admin_login")
                    sharedViewModel.resetAdmin()
                }
                .setNegativeButton("No") { _, _ ->
                    //
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}