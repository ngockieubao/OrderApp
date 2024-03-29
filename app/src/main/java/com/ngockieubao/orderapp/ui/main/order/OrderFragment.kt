package com.ngockieubao.orderapp.ui.main.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.data.Product
import com.ngockieubao.orderapp.databinding.FragmentOrderBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import com.ngockieubao.orderapp.util.Utils
import com.ngockieubao.orderapp.util.setUrl

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
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
        _binding = FragmentOrderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments ?: return
        val args = OrderFragmentArgs.fromBundle(bundle)
        val item = args.item

//        lifecycleScope.launch {
//            if (item != null) {
//                sharedViewModel.checkItemInCart(item).collect() {
//                    binding.tvAddToCart.text = "Added to cart"
//                    binding.imageButtonCart.visibility = View.GONE
//                    binding.constraintAddToCart.isClickable = false
//                    binding.constraintAddToCart.setBackgroundResource(R.color.green)
//                }
//            }
//        }

        showItemInfoFromHome(item)
        adjustQuantity()

        binding.constraintAddToCart.setOnClickListener {
            if (item == null) return@setOnClickListener
            sharedViewModel.createOrder(
                sharedViewModel.itemCartId.value!!,
                item.url,
                item.name,
                item.description,
                item.price,
                sharedViewModel.finalQuantity.value!!
            )
            val action = OrderFragmentDirections.actionOrderFragmentToHomeFragment()
            Toast.makeText(requireActivity(), "Added to cart", Toast.LENGTH_LONG).show()
            this.findNavController().navigate(action)
            resetOrder()
        }

        binding.imageButtonBack.setOnClickListener {
            this.findNavController().navigateUp()
            resetOrder()
        }
    }

    private fun showItemInfoFromHome(item: Product?) {
        if (item == null) return
        else {
            binding.apply {
                imageViewDetailOrder.setUrl(item.url)
                tvOrderName.text = item.name
                textViewWeight.text = item.weight
                textViewExpiry.text = item.expiry
                textViewDelivery.text = item.delivery
                textViewPrice.text = Utils.formatPrice(item.price)
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