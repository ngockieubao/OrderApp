package com.ngockieubao.orderapp.ui.main.contact

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.BtmSheetFragmentFeedbackBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel

class FeedbackBtmSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BtmSheetFragmentFeedbackBinding
    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BtmSheetFragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var fullName: String? = null
        var phoneNumber: String? = null
        var email: String? = null
        var comment: String? = null

        binding.apply {
            editTextName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        fullName = s.toString()
                    } else Toast.makeText(requireActivity(), "fullName is not empty", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            editTextPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        phoneNumber = s.toString()
                    } else Toast.makeText(requireActivity(), "phoneNumber is not empty", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            editTextEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        email = s.toString()
                    } else Toast.makeText(requireActivity(), "email is not empty", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            editTextFeedbackMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        comment = s.toString()
                    } else Toast.makeText(requireActivity(), "comment is not empty", Toast.LENGTH_SHORT)
                        .show()
                }
            })

            binding.btnUpdate.setOnClickListener {
                if (fullName == null ||
                    phoneNumber == null ||
                    email == null ||
                    comment == null
                ) {
                    Toast.makeText(
                        requireActivity(),
                        "Vui lòng điền vào trường còn trống",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    sharedViewModel.sendFeedback(fullName!!, phoneNumber!!, email!!, comment!!)
                    Toast.makeText(requireActivity(), "Gửi thành công", Toast.LENGTH_SHORT).show()
                    this@FeedbackBtmSheetFragment.dismiss()
                }
            }
        }
    }
}