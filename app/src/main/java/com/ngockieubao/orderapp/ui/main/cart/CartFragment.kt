package com.ngockieubao.orderapp.ui.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.data.Order
import com.ngockieubao.orderapp.databinding.FragmentCartBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import kotlinx.coroutines.launch

class CartFragment : Fragment(), DeleteInterface {

    private var _binding: FragmentCartBinding? = null
    private val binding
        get() = _binding!!

    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCart()

        val rcvOrder = binding.rcvOrderInfo
        val adapterOrder = OrderListAdapter({}, this)

        rcvOrder.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rcvOrder.adapter = adapterOrder

        lifecycle.coroutineScope.launch {
            sharedViewModel.getAllOrder().collect() {
                adapterOrder.submitList(it)
            }
        }

        lifecycle.coroutineScope.launch {
            sharedViewModel.calOrder(sharedViewModel.fullOrder())
        }
        sharedViewModel.sumOrder.observe(this.viewLifecycleOwner) {
            binding.tvTotalPrice.text = it.toString()
        }

        binding.btnCheckout.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToConfirmOrderFragment()
            this.findNavController().navigate(action)
        }

        binding.imageButtonBack.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    override fun deleteItemOrder(order: Order?) {
        if (order != null) {
            sharedViewModel.deleteOrder(order)
        }
    }

    private fun checkCart() {
        sharedViewModel.itemInCart.observe(this.viewLifecycleOwner) {
            if (sharedViewModel.itemInCart.value == 0) {
                binding.apply {
                    tvCheckCart.visibility = View.VISIBLE
                    rcvOrderInfo.visibility = View.GONE
                    constraintLayoutReceipt.visibility = View.GONE
                }
            } else {
                binding.tvCheckCart.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}