package com.ngockieubao.orderapp.ui.login.admin.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.AdminViewModelFactory
import com.ngockieubao.orderapp.data.Product
import com.ngockieubao.orderapp.databinding.FragmentManageProductBinding
import com.ngockieubao.orderapp.ui.login.admin.AdminViewModel
import kotlinx.coroutines.launch

class ManageProductFragment : Fragment(), MgrInterface {

    private lateinit var binding: FragmentManageProductBinding
    private val mAdminViewModel: AdminViewModel by activityViewModels {
        AdminViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rcv = binding.rcvProductManager
        val adapter = ProductMgrListAdapter({}, this)
        var type: String? = null

        rcv.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rcv.adapter = adapter

        initSpn()
        binding.spnCategoryProduct.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    type = parent?.getItemAtPosition(position).toString()
                    type?.let { switchFilter(it) }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        mAdminViewModel.listProduct.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.floatingBtnAddProduct.setOnClickListener {
            val action = ManageProductFragmentDirections.actionManageProductFragmentToAddProductFragment()
            this@ManageProductFragment.findNavController().navigate(action)
        }

        binding.imgvBtnBack.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun initSpn() {
        val spinner = binding.spnCategoryProduct
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spn_manage_product,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    private fun switchFilter(type: String) {
        when (type) {
            "Tất cả" -> {
                lifecycle.coroutineScope.launch {
                    mAdminViewModel.getAllProduct()
                }
            }
            else -> {
                lifecycle.coroutineScope.launch {
                    mAdminViewModel.getProductByFilter(type)
                }
            }
        }
    }

    override fun clickToMgr(item: Product?) {
        if (item == null) return
        else {
            val action = ManageProductFragmentDirections.actionManageProductFragmentToMgrProductDetail(item)
            this@ManageProductFragment.findNavController().navigate(action)
        }
    }
}