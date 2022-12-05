package com.ngockieubao.orderapp.ui.main.cart

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.data.Order
import com.ngockieubao.orderapp.databinding.FragmentBtmSheetConfirmOrderBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import com.ngockieubao.orderapp.util.Utils
import kotlinx.coroutines.launch

class ConfirmOrderBtmSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentBtmSheetConfirmOrderBinding? = null
    private val binding
        get() = _binding!!

    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    private var bundleGetListOrder: List<Order>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBtmSheetConfirmOrderBinding.inflate(inflater, container, false)

        val spinner = binding.spinnerSelectPurchase
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(requireActivity(), R.array.spinner_purchase, android.R.layout.simple_spinner_item).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        bundleGetListOrder = arguments?.getParcelableArrayList("myKey")

        val rcvListOrder = binding.rcvReceiptDetail
        val adapterListOrder = ConfirmOrderListAdapter()

        rcvListOrder.adapter = adapterListOrder
        showListOrder(adapterListOrder, bundleGetListOrder)

        lifecycleScope.launch {
            sharedViewModel.calOrder().collect {
                binding.tvTotalReceiptValue.text = Utils.formatPrice(it)
            }
        }

        return binding.root
    }

    private fun showListOrder(adapterListOrder: ConfirmOrderListAdapter, bundle: List<Order>?) {
        if (bundle != null) {
            lifecycleScope.launch {
                adapterListOrder.submitList(bundleGetListOrder)
            }
        } else {
            Toast.makeText(requireActivity(), "bundleGetListOrder: $bundleGetListOrder", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var name: String? = null
        var contact: String? = null
        var address: String? = null
        var note: String? = null

        binding.edtInputPurchaseName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    name = s.toString()
                } else Toast.makeText(requireActivity(), "name is not empty", Toast.LENGTH_SHORT).show()
            }
        })

        binding.edtInputPurchaseContact.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length > 12) {
                    binding.edtInputPurchaseContact.error = "No more!"
                }
                if (s.isEmpty()) {
                    binding.edtInputPurchaseContact.error = null
                    Log.d("LoginFragment", "onCreateView: $contact")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    contact = s.toString()
                } else {
                    binding.edtInputPurchaseContact.error = null
                    Toast.makeText(requireActivity(), "Contact is not empty", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.edtInputPurchaseAddress.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    address = s.toString()
                } else Toast.makeText(requireActivity(), "address is not empty", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnConfirm.setOnClickListener {
            if (contact == null ||
//                name == null ||
                    address == null) {
                lifecycle.coroutineScope.launch {
                    sharedViewModel.makeReceipt("Vagabond", "0382320936", "hanoi", "Non-note")
                }
                Toast.makeText(requireActivity(), "fields are not empty", Toast.LENGTH_SHORT).show()
            } else {
                lifecycle.coroutineScope.launch {
                    sharedViewModel.makeReceipt("Vagabond", contact!!, address!!, "Non-note")
                }
            }
        }
        sharedViewModel.receipt.observe(this.viewLifecycleOwner) {
            if (it == null) return@observe
            else {
                val action = ConfirmOrderBtmSheetDirections.actionConfirmOrderFragmentToHomeFragment()
                this.findNavController().navigate(action)
                Toast.makeText(requireActivity(), "Đặt hàng thành công - 0382320936 - Vagabond - hanoi", Toast.LENGTH_SHORT).show()
                sharedViewModel.resetMakeReceipt()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}