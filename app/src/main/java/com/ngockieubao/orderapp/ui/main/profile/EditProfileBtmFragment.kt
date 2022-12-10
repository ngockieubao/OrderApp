package com.ngockieubao.orderapp.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.databinding.BtmSheetFragmentEditProfileBinding

class EditProfileBtmFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BtmSheetFragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BtmSheetFragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdate.setOnClickListener {
            val value = binding.editTextUpdate.text.toString()
            val bundle = bundleOf("key" to value)
//            val action = EditProfileBtmFragmentDirections.actionEditProfileBtmFragmentToProfileFragment(value)
            this.findNavController().navigate(R.id.action_editProfileBtmFragment_to_profileFragment, bundle)
        }
    }
}