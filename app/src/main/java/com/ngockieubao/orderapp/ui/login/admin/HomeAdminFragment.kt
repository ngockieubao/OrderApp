package com.ngockieubao.orderapp.ui.login.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentHomeAdminBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel

class HomeAdminFragment : Fragment() {

    private lateinit var binding: FragmentHomeAdminBinding
    private lateinit var dialog: SignOutAdminDialog
    private val mOrderViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintLayoutManageReceipt.setOnClickListener {
            val action = HomeAdminFragmentDirections.actionHomeAdminFragmentToManageInvoiceFragment()
            this.findNavController().navigate(action)
        }
        binding.constraintLayoutStatistic.setOnClickListener { }
        binding.constraintLayoutUpdateProduct.setOnClickListener { }

        binding.constraintLayoutSignOut.setOnClickListener {
            dialog = SignOutAdminDialog()
            dialog.show(childFragmentManager, "admin_sign_out")
            mOrderViewModel.hasAdmin.observe(this.viewLifecycleOwner) {
                if (it == null) return@observe
                if (it == false) {
                    val action = HomeAdminFragmentDirections.actionHomeAdminFragmentToWelcomeFragment()
                    this@HomeAdminFragment.findNavController().navigate(action)
                }
            }
        }
    }
}