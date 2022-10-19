package com.ngockieubao.orderapp.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import java.lang.IllegalStateException

class SignOutDialog : DialogFragment() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            LoginViewModel.LoginViewModelFactory(requireActivity().application)
        )[LoginViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure to log out?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    loginViewModel.signOut()
                    this.findNavController().navigate(R.id.action_homeFragment_to_welcomeFragment)
                    Toast.makeText(requireActivity(), "Signed out", Toast.LENGTH_SHORT).show()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    //
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}