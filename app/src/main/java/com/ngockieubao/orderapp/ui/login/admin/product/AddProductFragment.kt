package com.ngockieubao.orderapp.ui.login.admin.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.AdminViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentAddProductBinding
import com.ngockieubao.orderapp.ui.login.admin.AdminViewModel

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private val mAdminViewModel: AdminViewModel by activityViewModels {
        AdminViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpn()

        var name: String? = null
        var price: String? = null
        var expiry: String? = null
        var weight: String? = null
        var description: String? = null
        var category: Int? = null
        var type: String? = null

        binding.spnAddProductType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    type = parent?.getItemAtPosition(position).toString()
                    category = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.apply {
            edtAddProductName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        name = s.toString()
                    } else {
                        Toast.makeText(requireActivity(), "name is not empty", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            edtAddProductPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        price = s.toString()
                    } else {
                        Toast.makeText(requireActivity(), "price is not empty", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            edtAddProductExpiry.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        expiry = s.toString()
                    } else {
                        Toast.makeText(requireActivity(), "expiry is not empty", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            edtAddProductWeight.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        weight = s.toString()
                    } else {
                        Toast.makeText(requireActivity(), "weight is not empty", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            edtAddProductDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        description = s.toString()
                    } else {
                        Toast.makeText(requireActivity(), "description is not empty", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        binding.btnAdd.setOnClickListener {
            if (name == null || name == "") {
                Toast.makeText(requireActivity(), "Tên sản phẩm không để trống", Toast.LENGTH_SHORT).show()
            } else if (price == null || price == "") {
                Toast.makeText(requireActivity(), "Giá sản phẩm không để trống", Toast.LENGTH_SHORT).show()
            } else if (expiry == null || expiry == "") {
                Toast.makeText(requireActivity(), "Hạn sử dụng sản phẩm không để trống", Toast.LENGTH_SHORT).show()
            } else if (weight == null || weight == "") {
                Toast.makeText(requireActivity(), "Khối lượng sản phẩm không để trống", Toast.LENGTH_SHORT).show()
            } else if (description == null || description == "") {
                Toast.makeText(requireActivity(), "Mô tả sản phẩm không để trống", Toast.LENGTH_SHORT).show()
            } else if (category == null) {
                Toast.makeText(requireActivity(), "category null", Toast.LENGTH_SHORT).show()
            } else if (type == "Chọn mục" || type == null) {
                Toast.makeText(requireActivity(), "Unselect", Toast.LENGTH_SHORT).show()
            } else {
                addProduct(name!!, price!!, expiry!!, weight!!, description!!, category!!, type!!)
                mAdminViewModel.isAdded.observe(this.viewLifecycleOwner) {
                    if (it == null) return@observe
                    if (it == true) {
                        this@AddProductFragment.findNavController().navigateUp()
                        mAdminViewModel.resetAdded()
                    }
                }
            }
        }
    }

    private fun addProduct(
        name: String,
        price: String,
        expiry: String,
        weight: String,
        description: String,
        category: Int,
        type: String
    ) {
        mAdminViewModel.addProduct(name, price, expiry, weight, description, category, type)
    }

    private fun initSpn() {
        val spinner = binding.spnAddProductType
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spn_add_product_manager,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

}