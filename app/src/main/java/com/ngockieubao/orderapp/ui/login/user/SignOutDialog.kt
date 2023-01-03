package com.ngockieubao.orderapp.ui.login.user

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ngockieubao.orderapp.base.LoginViewModelFactory
import com.ngockieubao.orderapp.ui.login.LoginViewModel
import java.lang.IllegalStateException

class SignOutDialog : DialogFragment() {

    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(requireActivity().application)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure to log out?")
                .setPositiveButton("Yes") { dialog, id ->
                    loginViewModel.signOut()
                    Toast.makeText(requireActivity(), "Signed out", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { _, _ ->
                    //
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}