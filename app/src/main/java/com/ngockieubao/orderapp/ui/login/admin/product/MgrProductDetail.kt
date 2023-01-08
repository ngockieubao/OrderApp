package com.ngockieubao.orderapp.ui.login.admin.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.data.Product
import com.ngockieubao.orderapp.databinding.FragmentMgrProductDetailBinding
import com.ngockieubao.orderapp.util.Utils
import com.ngockieubao.orderapp.util.Utils.toEditable
import com.ngockieubao.orderapp.util.setUrl

class MgrProductDetail : Fragment() {

    private lateinit var binding: FragmentMgrProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMgrProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments ?: return
        val args = MgrProductDetailArgs.fromBundle(bundle)
        val item = args.item

        initSpn()
        showInfo(item)
//        setCategoryProduct()

        binding.imgvBtnBack.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun setCategoryProduct() {
        var type: String? = null

        binding.spnProductType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    type = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun initSpn() {
        val spinner = binding.spnProductType
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spn_product_manager,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    private fun showInfo(item: Product?) {
        if (item == null) return
        binding.apply {
            edtProductName.text = item.name.toEditable()
            edtProductPrice.text = Utils.formatPrice(item.price).toEditable()
            edtProductExpiry.text = item.expiry.toEditable()
            edtProductWeight.text = item.weight.toEditable()
            edtProductDescription.text = item.description.toEditable()
            imgvProduct.setUrl(item.url)
            spnProductType.setSelection(item.category.minus(1))
        }
    }
}