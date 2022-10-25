package com.ngockieubao.orderapp.ui.main.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngockieubao.orderapp.data.Order
import com.ngockieubao.orderapp.databinding.FragmentCartBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel

class CartFragment : Fragment(), DeleteInterface {

    private var _binding: FragmentCartBinding? = null
    private val binding
        get() = _binding!!

    private val shareViewModel: OrderViewModel by activityViewModels()

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

        val rcvOrder = binding.rcvOrderInfo
        val adapterOrder = OrderListAdapter {}

        rcvOrder.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rcvOrder.adapter = adapterOrder

        shareViewModel.listOrder.observe(this.viewLifecycleOwner) {
            adapterOrder.submitList(it)
        }

        binding.imageButtonBack.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }


//    private fun adjustQuantity() {
//        binding.textViewQuantity.text = sharedViewModel.defaultQuantity.toString()
//
//        binding.textViewQuantityDecrease.setOnClickListener {
//            sharedViewModel.decreasing()
//            sharedViewModel.finalQuantity.observe(this.viewLifecycleOwner) {
//                if (it == null) return@observe
//                else binding.textViewQuantity.text = it.toString()
//            }
//        }
//
//        binding.textViewQuantityIncrease.setOnClickListener {
//            sharedViewModel.increasing()
//            sharedViewModel.finalQuantity.observe(this.viewLifecycleOwner) {
//                if (it == null) return@observe
//                else binding.textViewQuantity.text = it.toString()
//            }
//        }

    override fun deleteOrder(order: Order?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}