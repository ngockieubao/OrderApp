package com.ngockieubao.orderapp.ui.main.cart

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.data.Order
import com.ngockieubao.orderapp.databinding.BtmSheetFragmentConfirmOrderBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import com.ngockieubao.orderapp.util.Utils
import kotlinx.coroutines.launch

class ConfirmOrderBtmSheet : BottomSheetDialogFragment() {

    private var _binding: BtmSheetFragmentConfirmOrderBinding? = null
    private val binding
        get() = _binding!!

    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    private var bundleGetListOrder: List<Order>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = BtmSheetFragmentConfirmOrderBinding.inflate(inflater, container, false)

        bundleGetListOrder = arguments?.getParcelableArrayList("myKey")

        val rcvListOrder = binding.rcvReceiptDetail
        val adapterListOrder = ConfirmOrderListAdapter()
        val spinner = binding.spinnerSelectPurchase

        rcvListOrder.adapter = adapterListOrder
        showListOrder(adapterListOrder, bundleGetListOrder)
        initSpn(spinner)

        lifecycleScope.launch {
            sharedViewModel.calOrder().collect {
                binding.tvTotalReceiptValue.text = Utils.formatPrice(it)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var address: String? = null
        var contact: String? = null
        var name: String? = null
        var note: String? = null
        var type: String? = null

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

        binding.spinnerSelectPurchase.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnConfirm.setOnClickListener {
            if (
                address == null ||
                contact == null ||
                name == null ||
                type == null
            ) {
                lifecycle.coroutineScope.launch {
                    sharedViewModel.makeReceipt(
                        address = "hanoi",
                        contact = "0382320936",
                        name = "Vagabond",
                        note = "Non-note",
                        type = "Thanh toán khi nhận hàng"
                    )
                }
                Toast.makeText(requireActivity(), "fields are not empty", Toast.LENGTH_SHORT).show()
            } else {
                lifecycle.coroutineScope.launch {
                    sharedViewModel.makeReceipt(
                        address = address!!,
                        contact = contact!!,
                        name = name!!,
                        note = "Non-note",
                        type = type!!
                    )
                }
            }
            checkMakeReceipt()
        }
    }

    private fun checkMakeReceipt() {
        sharedViewModel.receipt.observe(this.viewLifecycleOwner) {
            if (it == null) return@observe
            else {
                val action = ConfirmOrderBtmSheetDirections.actionConfirmOrderFragmentToHomeFragment()
                this@ConfirmOrderBtmSheet.findNavController().navigate(action)
                Toast.makeText(requireActivity(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
                sharedViewModel.resetMakeReceipt()
                lifecycleScope.launch {
                    sharedViewModel.clearCart()
                }
            }
        }
    }

    private fun initSpn(spinner: Spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spinner_purchase,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    private fun showListOrder(adapterListOrder: ConfirmOrderListAdapter, bundle: List<Order>?) {
        if (bundle != null) {
            lifecycleScope.launch {
                adapterListOrder.submitList(bundleGetListOrder)
            }
        } else {
            Toast.makeText(requireActivity(), "bundleGetListOrder: $bundleGetListOrder", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}