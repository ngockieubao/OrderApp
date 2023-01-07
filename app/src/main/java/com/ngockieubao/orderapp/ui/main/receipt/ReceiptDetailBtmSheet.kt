package com.ngockieubao.orderapp.ui.main.receipt

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
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.data.Receipt
import com.ngockieubao.orderapp.databinding.BtmSheetFragmentReceiptDetailBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import com.ngockieubao.orderapp.util.Utils

class ReceiptDetailBtmSheet : BottomSheetDialogFragment() {

    private var _binding: BtmSheetFragmentReceiptDetailBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = BtmSheetFragmentReceiptDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments ?: return
        val args = ReceiptDetailBtmSheetArgs.fromBundle(bundle)
        val item = args.item

        val rcv = binding.rcvListProductOrdered
        val adapter = OrderedListAdapter()

        rcv.layoutManager =
            GridLayoutManager(requireActivity(), 2, GridLayoutManager.HORIZONTAL, false)

        showItemInfoFromHome(item, adapter)

        var valueUpdate: String? = null
        initSpn()

        binding.spinnerUpdateReceipt.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    valueUpdate = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.btnConfirm.setOnClickListener {
            if (item == null) return@setOnClickListener
            if (valueUpdate == "Chọn mục") {
                Toast.makeText(requireActivity(), "Unselect", Toast.LENGTH_SHORT).show()
            } else {
                valueUpdate?.let { value -> sharedViewModel.cancelReceipt(item.code, value) }
                this@ReceiptDetailBtmSheet.dismiss()
            }
        }
    }

    private fun initSpn() {
        val spinner = binding.spinnerUpdateReceipt
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spinner_update_receipt,
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
            tvReceiptCodeValue.text = item.code
            tvReceiptUsernameValue.text = item.name
            tvReceiptPhoneNumberValue.text = item.contact
            tvReceiptAddressValue.text = item.address
            tvReceiptTimeOrderValue.text = item.time
            tvReceiptStatusValue.text = item.status
            tvReceiptTotalPurchaseValue.text = Utils.formatPrice(item.total)
            tvReceiptTypePurchaseValue.text = item.type
            rcvListProductOrdered.adapter = adapter
            adapter.submitList(item.receipts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}