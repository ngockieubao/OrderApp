package com.ngockieubao.orderapp.ui.login.admin

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import java.lang.IllegalStateException

class SignOutAdminDialog : DialogFragment() {

    private val orderViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure to log out?")
                .setPositiveButton("Yes") { dialog, id ->
                    orderViewModel.signOutAdmin()
                }
                .setNegativeButton("No") { _, _ ->
                    // do nothing
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}