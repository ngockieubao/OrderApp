package com.ngockieubao.orderapp.ui.login.user

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
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
                }
                .setNegativeButton("No") { _, _ ->
                    // do nothing
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}