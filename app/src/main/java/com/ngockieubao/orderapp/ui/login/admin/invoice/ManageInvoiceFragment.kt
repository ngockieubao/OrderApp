package com.ngockieubao.orderapp.ui.login.admin.invoice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.AdminViewModelFactory
import com.ngockieubao.orderapp.data.Receipt
import com.ngockieubao.orderapp.databinding.FragmentManageInvoiceBinding
import com.ngockieubao.orderapp.ui.login.admin.AdminViewModel
import com.ngockieubao.orderapp.ui.main.receipt.UpdateInterface
import kotlinx.coroutines.launch

class ManageInvoiceFragment : Fragment(), UpdateInterface {

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
        val adapter = InvoiceListAdapter(this@ManageInvoiceFragment)
        var type: String? = null

        rcv.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rcv.adapter = adapter

        sharedViewModel.listInvoice.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.spnCategoryInvoice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = parent?.getItemAtPosition(position).toString()
                type?.let { switchFilter(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
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

    private fun switchFilter(status: String) {
        when (status) {
            "Tất cả" -> {
                lifecycle.coroutineScope.launch {
                    sharedViewModel.getAllInvoice()
                }
            }
            else -> {
                lifecycle.coroutineScope.launch {
                    sharedViewModel.getInvoiceByFilter(status)
                }
            }
        }
    }

    override fun clickToUpdateReceipt(item: Receipt?) {
        if (item == null) return
        else {
            val action = ManageInvoiceFragmentDirections.actionManageInvoiceFragmentToInvoiceDetailBtmSheet(item)
            this@ManageInvoiceFragment.findNavController().navigate(action)
        }
    }
}