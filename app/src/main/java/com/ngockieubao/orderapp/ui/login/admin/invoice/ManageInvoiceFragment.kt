package com.ngockieubao.orderapp.ui.login.admin.invoice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.AdminViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentManageInvoiceBinding
import com.ngockieubao.orderapp.ui.login.admin.AdminViewModel
import kotlinx.coroutines.launch

class ManageInvoiceFragment : Fragment() {

    private lateinit var binding: FragmentManageInvoiceBinding
    private val sharedViewModel: AdminViewModel by activityViewModels {
        AdminViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageInvoiceBinding.inflate(inflater, container, false)
        initSpn()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rcv = binding.rcvInvoice
        val adapter = InvoiceListAdapter()

        rcv.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rcv.adapter = adapter

        lifecycle.coroutineScope.launch {
            sharedViewModel.getAllInvoice()
        }

        sharedViewModel.listInvoice.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.imgvBtnBack.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun initSpn() {
        val spinner = binding.spnCategoryInvoice
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spinner_manage_invoice,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }
}