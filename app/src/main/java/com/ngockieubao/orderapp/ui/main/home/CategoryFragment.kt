package com.ngockieubao.orderapp.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.data.Category
import com.ngockieubao.orderapp.databinding.FragmentCategoryBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val orderViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments ?: return
        val args = CategoryFragmentArgs.fromBundle(bundle)
        val item = args.item ?: return

        val rcvCategoryDetail = binding.rcvCategoryDetail
        val adapterCategoryDetail = ProductCategoryAdapter {
            val action = CategoryFragmentDirections.actionCategoryFragmentToOrderFragment(it)
            this@CategoryFragment.findNavController().navigate(action)
        }

        rcvCategoryDetail.adapter = adapterCategoryDetail
        populateCategory(item)

        orderViewModel.listProductCategory.observe(this.viewLifecycleOwner) {
            adapterCategoryDetail.submitList(it)
        }

        binding.imageViewBtnBack.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun populateCategory(item: Category) {
        lifecycleScope.launch {
            when (item.name) {
                "Cơm" -> {
                    // handles run by coroutine: load data done -> populate ui
                    delay(1000)
                    binding.tvCategoryTitleHeader.text = item.name
                    orderViewModel.getProductByCategory(item.name)
                }
                "Phở" -> {
                    binding.tvCategoryTitleHeader.text = item.name
                    orderViewModel.getProductByCategory(item.name)
                }
                "Lẩu" -> {
                    binding.tvCategoryTitleHeader.text = item.name
                    orderViewModel.getProductByCategory(item.name)
                }
                "Đồ ăn vặt" -> {
                    binding.tvCategoryTitleHeader.text = item.name
                    orderViewModel.getProductByCategory(item.name)
                }
                "Đồ ăn nhanh" -> {
                    binding.tvCategoryTitleHeader.text = item.name
                    orderViewModel.getProductByCategory(item.name)
                }
                "Đồ uống" -> {
                    binding.tvCategoryTitleHeader.text = item.name
                    orderViewModel.getProductByCategory(item.name)
                }
            }
        }
    }
}