package com.ngockieubao.orderapp.ui.main.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.data.Product
import com.ngockieubao.orderapp.databinding.FragmentOrderBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding
        get() = _binding!!

    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentOrderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments ?: return
        val args = OrderFragmentArgs.fromBundle(bundle)
        val item = args.item

        showItemInfoFromHome(item)
        adjustQuantity()

        binding.constraintAddToCart.setOnClickListener {
            if (item == null) return@setOnClickListener
            sharedViewModel.createOrder(item.url, item.name, item.description, item.price, sharedViewModel.finalQuantity.value!!)
            Toast.makeText(requireActivity(), "Added to cart", Toast.LENGTH_LONG)
                .show()
            this.findNavController().navigate(R.id.homeFragment)
            resetOrder()
        }

        binding.imageButtonBack.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun showItemInfoFromHome(item: Product?) {
        if (item == null) return
        else {
            binding.apply {
                tvOrderName.text = item.name
                textViewWeight.text = item.weight
                textViewExpiry.text = item.expiry
                textViewRating.text = item.rating.toString()
                textViewPrice.text = item.price.toString()
                textViewDescriptionContent.text = item.description
            }
        }
    }

    private fun adjustQuantity() {
        binding.textViewQuantity.text = sharedViewModel.defaultQuantity.toString()

        binding.btnQuantityDecrease.setOnClickListener {
            sharedViewModel.decreasing()
            sharedViewModel.finalQuantity.observe(this.viewLifecycleOwner) {
                if (it == null) return@observe
                else binding.textViewQuantity.text = it.toString()
            }
        }

        binding.btnQuantityIncrease.setOnClickListener {
            sharedViewModel.increasing()
            sharedViewModel.finalQuantity.observe(this.viewLifecycleOwner) {
                if (it == null) return@observe
                else binding.textViewQuantity.text = it.toString()
            }
        }
    }

    private fun resetOrder() {
        sharedViewModel.resetOrder()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}