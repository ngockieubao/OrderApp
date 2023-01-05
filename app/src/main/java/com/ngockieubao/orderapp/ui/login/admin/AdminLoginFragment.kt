package com.ngockieubao.orderapp.ui.login.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentAdminLoginBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel

class AdminLoginFragment : Fragment() {

    private lateinit var binding: FragmentAdminLoginBinding
    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                } else
                    Toast.makeText(requireActivity(), "Email is not empty", Toast.LENGTH_SHORT).show()
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
                } else
                    Toast.makeText(requireActivity(), "Password is not empty", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnLoginNow.setOnClickListener {
            if (email == null || passwd == null) {
                Toast.makeText(requireActivity(), "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show()
            } else {
                sharedViewModel.signInAdmin(email!!, passwd!!)
                sharedViewModel.admin.observe(this.viewLifecycleOwner) {
                    if (it == null) return@observe
                    else {
                        Log.d(TAG, "onCreateView: $it")
                        Toast.makeText(requireActivity(), "Welcome, admin!", Toast.LENGTH_SHORT).show()
                        val action =
                            AdminLoginFragmentDirections.actionAdminLoginFragmentToHomeAdminFragment()
                        this.findNavController().navigate(action)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AdminLoginFragment"
    }
}