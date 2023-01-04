package com.ngockieubao.orderapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.data.Category
import com.ngockieubao.orderapp.databinding.FragmentHomeBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import com.ngockieubao.orderapp.util.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3


class HomeFragment : Fragment(), SwitchCategoryInterface {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mViewPager2: ViewPager2
    private lateinit var mCircleIndicator3: CircleIndicator3

    private val orderViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View
        mViewPager2 = binding.viewPager2
        mCircleIndicator3 = binding.circleIndicator3
        val rcvCategory = binding.rcvCategory
        val rcvProduct = binding.rcvProduct
        val rcvProductPopular = binding.rcvProductPopular

        // Adapter
        val bannerViewPager2Adapter = BannerViewPager2Adapter()
        val adapterCategory = CategoryListAdapter({}, this)
        val adapterProduct = ProductListAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToOrderFragment(it)
            this.findNavController().navigate(action)
        }
        val adapterProductPopular = ProductListAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToOrderFragment(it)
            this.findNavController().navigate(action)
        }

        // Set adapter
        mViewPager2.adapter = bannerViewPager2Adapter
        mCircleIndicator3.setViewPager(mViewPager2)

        rcvCategory.layoutManager =
            GridLayoutManager(this.context, 3)
        rcvCategory.adapter = adapterCategory

        rcvProduct.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rcvProduct.adapter = adapterProduct

        rcvProductPopular.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rcvProductPopular.adapter = adapterProductPopular

        // Flow
        binding.tvWelcome.text = Utils.formatWelcome(orderViewModel.checkCurrentUser()?.displayName!!)

        lifecycleScope.launch {
            orderViewModel.getProduct()
            orderViewModel.getProductPopular()
            orderViewModel.getBanner()
        }

        var size = 0
        orderViewModel.banner.observe(this@HomeFragment.viewLifecycleOwner) {
            size = it
        }
        orderViewModel.listBanner.observe(this.viewLifecycleOwner) {
            if (it == null) return@observe
            bannerViewPager2Adapter.listData = it
        }

        mViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                lifecycleScope.launch {
                    if (mViewPager2.currentItem == size - 1) {
                        delay(3000)
                        mViewPager2.currentItem = 0
                    } else {
                        delay(3000)
                        mViewPager2.currentItem = mViewPager2.currentItem + 1
                    }
                }
            }
        })

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

    override fun clickToSwitchCategory(item: Category?) {
        if (item == null) return
        val action = HomeFragmentDirections.actionHomeFragmentToCategoryFragment(item)
        this.findNavController().navigate(action)
    }
}