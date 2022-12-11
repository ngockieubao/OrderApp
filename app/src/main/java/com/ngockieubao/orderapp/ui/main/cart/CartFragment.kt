package com.ngockieubao.orderapp.ui.main.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.data.Order
import com.ngockieubao.orderapp.databinding.FragmentCartBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import com.ngockieubao.orderapp.util.Utils
import kotlinx.coroutines.launch

class CartFragment : Fragment(), UpdateInterface {

    private lateinit var binding: FragmentCartBinding
    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rcvOrder = binding.rcvOrderInfo
        val adapterOrder = OrderListAdapter({}, this)

        rcvOrder.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rcvOrder.adapter = adapterOrder

        lifecycle.coroutineScope.launch {
            sharedViewModel.getAllOrderFlow().collect {
                if (it.isNotEmpty()) {
                    binding.apply {
                        tvCheckCart.visibility = View.GONE
                        rcvOrderInfo.visibility = View.VISIBLE
                        constraintLayoutReceipt.visibility = View.VISIBLE
                    }
                    adapterOrder.submitList(it)
                } else {
                    binding.apply {
                        tvCheckCart.visibility = View.VISIBLE
                        rcvOrderInfo.visibility = View.GONE
                        constraintLayoutReceipt.visibility = View.GONE
                    }
                }
            }
        }
        lifecycle.coroutineScope.launch {
            sharedViewModel.calOrder().collect {
                binding.tvTotalPrice.text = Utils.formatPrice(it)
            }
        }

        binding.btnCheckout.setOnClickListener {
            try {
                lifecycleScope.launch {
                    val listOrder = sharedViewModel.getAllOrder()
                    val bundle = bundleOf("myKey" to listOrder)
                    this@CartFragment.findNavController()
                        .navigate(R.id.action_cartFragment_to_confirmOrderFragment, bundle)
                }
            } catch (ex: Exception) {
                Log.d("CartFragment", "onViewCreated: $ex")
            }
        }
    }

    override fun deleteItemOrder(order: Order?) {
        if (order != null) {
            sharedViewModel.deleteOrder(order)
        }
    }

    override fun increaseQuantity(name: String, quantity: Int) {
        lifecycleScope.launch {
            sharedViewModel.increasingCart(name, quantity)
        }
    }

    override fun decreaseQuantity(name: String, quantity: Int) {
        lifecycleScope.launch {
            sharedViewModel.decreasingCart(name, quantity)
        }
    }
}