package com.ngockieubao.orderapp.ui.login.admin.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.data.Receipt
import com.ngockieubao.orderapp.databinding.FragmentInvoiceDetailBtmSheetBinding
import com.ngockieubao.orderapp.ui.main.receipt.OrderedListAdapter
import com.ngockieubao.orderapp.util.Utils

class InvoiceDetailBtmSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentInvoiceDetailBtmSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInvoiceDetailBtmSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments ?: return
        val args = InvoiceDetailBtmSheetArgs.fromBundle(bundle)
        val item = args.item

        val rcv = binding.rcvListProductOrdered
        val adapter = OrderedListAdapter()
        rcv.layoutManager =
            GridLayoutManager(requireActivity(), 2, GridLayoutManager.HORIZONTAL, false)

        showItemInfoFromHome(item, adapter)

        var valueUpdate: String? = null
        initSpn()
    }

    private fun initSpn() {
        val spinner = binding.spinnerUpdateInvoice
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spn_manage_invoice,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    private fun showItemInfoFromHome(item: Receipt?, adapter: OrderedListAdapter) {
        if (item == null) return
        binding.apply {
            tvInvoiceCodeValue.text = item.code
            tvInvoiceUsernameValue.text = item.name
            tvInvoicePhoneNumberValue.text = item.contact
            tvInvoiceAddressValue.text = item.address
            tvInvoiceTimeOrderValue.text = item.time
            tvInvoiceStatusValue.text = item.status
            tvInvoiceTotalPurchaseValue.text = Utils.formatPrice(item.total)
            rcvListProductOrdered.adapter = adapter
            adapter.submitList(item.receipts)
        }
    }
}