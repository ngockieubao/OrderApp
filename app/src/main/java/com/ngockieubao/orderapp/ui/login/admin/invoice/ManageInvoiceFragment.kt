package com.ngockieubao.orderapp.ui.login.admin.invoice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.databinding.FragmentManageInvoiceBinding
import com.ngockieubao.orderapp.ui.main.receipt.OrderedListAdapter

class ManageInvoiceFragment : Fragment() {

    private lateinit var binding: FragmentManageInvoiceBinding

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
        val adapter = OrderedListAdapter()

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