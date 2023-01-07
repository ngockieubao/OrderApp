package com.ngockieubao.orderapp.ui.login.admin.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.AdminViewModelFactory
import com.ngockieubao.orderapp.data.Receipt
import com.ngockieubao.orderapp.databinding.FragmentInvoiceDetailBtmSheetBinding
import com.ngockieubao.orderapp.ui.login.admin.AdminViewModel
import com.ngockieubao.orderapp.ui.main.receipt.OrderedListAdapter
import com.ngockieubao.orderapp.util.Utils

class InvoiceDetailBtmSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentInvoiceDetailBtmSheetBinding
    private val mAdminViewModel: AdminViewModel by activityViewModels {
        AdminViewModelFactory(requireActivity().application)
    }

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
        binding.spinnerUpdateInvoice.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    valueUpdate = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.btnConfirm.setOnClickListener {
            if (item == null) return@setOnClickListener
            if (valueUpdate == "Chọn mục") {
                Toast.makeText(requireActivity(), "Vui lòng chọn hình thức", Toast.LENGTH_SHORT).show()
            } else {
                valueUpdate?.let { value -> mAdminViewModel.updateInvoice(item.code, value) }
                this@InvoiceDetailBtmSheet.dismiss()
            }
        }
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