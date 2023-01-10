package com.ngockieubao.orderapp.ui.login.admin.product

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.AdminViewModelFactory
import com.ngockieubao.orderapp.data.Product
import com.ngockieubao.orderapp.databinding.FragmentMgrProductDetailBinding
import com.ngockieubao.orderapp.ui.login.admin.AdminViewModel
import com.ngockieubao.orderapp.util.Utils.toEditable
import com.ngockieubao.orderapp.util.setUrl

class MgrProductDetail : Fragment() {

    private lateinit var binding: FragmentMgrProductDetailBinding
    private val mAdminViewModel: AdminViewModel by activityViewModels {
        AdminViewModelFactory(requireActivity().application)
    }

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

        var category: Int? = null
        var type: String? = null

        binding.spnProductType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    type = parent?.getItemAtPosition(position).toString()
                    category = position + 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.btnSave.setOnClickListener {
            if (item != null) {
                val docID = item.docID
                val name = binding.edtProductName.text.toString()
                val price = binding.edtProductPrice.text.toString()
                val expiry = binding.edtProductExpiry.text.toString()
                val weight = binding.edtProductWeight.text.toString()
                val delivery = binding.edtProductDelivery.text.toString()
                val description = binding.edtProductDescription.text.toString()

                if (category == null) {
                    Toast.makeText(requireActivity(), "category null", Toast.LENGTH_SHORT).show()
                } else if (type == null) {
                    Toast.makeText(requireActivity(), "type null", Toast.LENGTH_SHORT).show()
                } else {
                    edtProduct(docID, name, price, category!!, expiry, type!!, weight, delivery, description)
                    mAdminViewModel.isSuccess.observe(this.viewLifecycleOwner) {
                        if (it == null) return@observe
                        if (it == true) {
                            mAdminViewModel.reset()
                            this@MgrProductDetail.findNavController().navigateUp()
                        }
                    }
                }
            }
        }

        binding.btnDel.setOnClickListener {
            if (item != null) {
                val docID = item.docID

                showDialog(docID)
                mAdminViewModel.isDelete.observe(this.viewLifecycleOwner) {
                    if (it == null) return@observe
                    if (it == true) {
                        mAdminViewModel.resetDel()
                        this@MgrProductDetail.findNavController().navigateUp()
                    }
                }
            }
        }

        binding.imgvBtnBack.setOnClickListener {
            this.findNavController().navigateUp()
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
            edtProductPrice.text = item.price.toString().toEditable()
            edtProductExpiry.text = item.expiry.toEditable()
            edtProductWeight.text = item.weight.toEditable()
            edtProductDelivery.text = item.delivery.toEditable()
            edtProductDescription.text = item.description.toEditable()
            imgvProduct.setUrl(item.url)
            spnProductType.setSelection(item.category.minus(1))
        }
    }

    private fun edtProduct(
        docID: String, name: String, price: String, category: Int,
        expiry: String, type: String, weight: String, delivery: String, description: String
    ) {
        mAdminViewModel.edtProduct(docID, name, price, category, expiry, type, weight, delivery, description)
    }

    private fun showDialog(docID: String) {
        val dialog = activity?.let { Dialog(it) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_dialog)
            val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
            val noBtn = dialog.findViewById(R.id.btn_no) as Button

            yesBtn.setOnClickListener {
                mAdminViewModel.delProduct(docID)
                dialog.dismiss()
            }
            noBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}