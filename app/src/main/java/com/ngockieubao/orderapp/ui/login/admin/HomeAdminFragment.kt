package com.ngockieubao.orderapp.ui.login.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentHomeAdminBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel

class HomeAdminFragment : Fragment() {

    private lateinit var binding: FragmentHomeAdminBinding
    private lateinit var dialog: SignOutAdminDialog
    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        dialog = SignOutAdminDialog()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintLayoutManageReceipt.setOnClickListener {
            this.findNavController().navigate(R.id.action_homeAdminFragment_to_manageInvoiceFragment)
        }
        binding.constraintLayoutStatistic.setOnClickListener { }
        binding.constraintLayoutUpdateProduct.setOnClickListener { }
        binding.constraintLayoutSignOut.setOnClickListener {
            dialog.show(childFragmentManager, "sign_out")
        }
    }
}