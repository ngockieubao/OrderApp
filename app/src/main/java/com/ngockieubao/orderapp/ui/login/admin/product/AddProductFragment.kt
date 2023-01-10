package com.ngockieubao.orderapp.ui.login.admin.product

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.base.AdminViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentAddProductBinding
import com.ngockieubao.orderapp.ui.login.admin.AdminViewModel
import com.ngockieubao.orderapp.util.Constants

@Suppress("DEPRECATION")
class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private val mAdminViewModel: AdminViewModel by activityViewModels {
        AdminViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpn()
        binding.constraintLayoutAddImageProductContainer.setOnClickListener {
            if (activity?.let { act ->
                    ActivityCompat.checkSelfPermission(
                        act, Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.LIBRARY_PICKER
                )
            } else {
                startGallery()
            }
        }
        binding.imgvBtnBack.setOnClickListener { this@AddProductFragment.findNavController().navigateUp() }
    }

    private fun initProduct(bitmap: Bitmap): Pair<Int?, String?> {
        var name: String? = null
        var price: String? = null
        var expiry: String? = null
        var weight: String? = null
        var delivery: String? = null
        var description: String? = null
        var category: Int? = null
        var type: String? = null

        binding.spnAddProductType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            edtAddProductDelivery.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        delivery = s.toString()
                    } else {
                        Toast.makeText(requireActivity(), "delivery is not empty", Toast.LENGTH_SHORT).show()
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
            } else if (delivery == null || delivery == "") {
                Toast.makeText(requireActivity(), "Thời gian giao hàng không để trống", Toast.LENGTH_SHORT).show()
            } else if (description == null || description == "") {
                Toast.makeText(requireActivity(), "Mô tả sản phẩm không để trống", Toast.LENGTH_SHORT).show()
            } else if (category == null) {
                Toast.makeText(requireActivity(), "category null", Toast.LENGTH_SHORT).show()
            } else if (type == "Chọn mục" || type == null) {
                Toast.makeText(requireActivity(), "Unselect", Toast.LENGTH_SHORT).show()
            } else {
                addProduct(name!!, price!!, expiry!!, weight!!, delivery!!, description!!, category!!, type!!, bitmap)
                mAdminViewModel.isAdded.observe(this.viewLifecycleOwner) {
                    if (it == null) return@observe
                    if (it == true) {
                        this@AddProductFragment.findNavController().navigateUp()
                        mAdminViewModel.resetAdded()
                    }
                }
            }
        }

        return Pair(category, type)
    }

    private fun addProduct(
        name: String,
        price: String,
        expiry: String,
        weight: String,
        delivery: String,
        description: String,
        category: Int,
        type: String,
        bitmap: Bitmap
    ) {
        mAdminViewModel.addProduct(name, price, expiry, weight, delivery, description, category, type, bitmap)
    }

    private fun initSpn() {
        val spinner = binding.spnAddProductType
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(), R.array.spn_add_product_manager, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // super method removed
        if (resultCode == RESULT_OK && requestCode == Constants.LIBRARY_PICKER && data != null) {
            val returnUri: Uri? = data.data
            val bitmapImage = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, returnUri)
            binding.imgvAddProduct.setImageBitmap(bitmapImage)
            initProduct(bitmapImage)
        }
    }

    @SuppressLint("IntentReset", "QueryPermissionsNeeded")
    private fun startGallery() {
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, Constants.LIBRARY_PICKER)
        }
    }

    private fun pickImage() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.LIBRARY_PICKER
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(
                Intent.createChooser(intent, "Select Image"), Constants.LIBRARY_PICKER
            )
        }
    }

    private fun getPicture(selectedImage: Uri?): Bitmap? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            requireContext().contentResolver.query(selectedImage!!, filePathColumn, null, null, null) ?: return null
        cursor.moveToFirst()
        val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
        val picturePath: String = cursor.getString(columnIndex)
        cursor.close()
        return BitmapFactory.decodeFile(picturePath)
    }
}