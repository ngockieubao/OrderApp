package com.ngockieubao.orderapp.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngockieubao.orderapp.databinding.FragmentHomeBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            OrderViewModel.OrderViewModelFactory(requireActivity().application)
        )[OrderViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rcvCategory = binding.rcvCategory
        val rcvProduct = binding.rcvProduct
        val rcvProductPopular = binding.rcvProductPopular

        val adapterCategory = CategoryListAdapter {}
        val adapterProduct = ProductListAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToOrderFragment(it)
            this.findNavController().navigate(action)
        }
        val adapterProductPopular = ProductListAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToOrderFragment(it)
            this.findNavController().navigate(action)
        }

        lifecycleScope.launch {
            orderViewModel.getProduct()
            orderViewModel.getProductPopular()
        }

        rcvCategory.layoutManager =
            GridLayoutManager(this.context, 3)
        rcvCategory.adapter = adapterCategory

        rcvProduct.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rcvProduct.adapter = adapterProduct

        rcvProductPopular.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rcvProductPopular.adapter = adapterProductPopular

        orderViewModel.listCategory.observe(this.viewLifecycleOwner) {
            adapterCategory.submitList(it)
        }

        orderViewModel.listProduct.observe(this.viewLifecycleOwner) {
            adapterProduct.submitList(it)
        }

        orderViewModel.listProductPopular.observe(this.viewLifecycleOwner) {
            adapterProductPopular.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}