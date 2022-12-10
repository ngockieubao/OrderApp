package com.ngockieubao.orderapp.ui.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentUserInfoBinding
import com.ngockieubao.orderapp.ui.login.SignOutDialog
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import com.ngockieubao.orderapp.util.setUrl
import kotlinx.coroutines.launch

class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding
    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }
    private lateinit var dialog: SignOutDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = SignOutDialog()
        binding.btnSignOut.setOnClickListener {
            dialog.show(childFragmentManager, "sign_out")
        }

        lifecycleScope.launch {
            sharedViewModel.getUserInfo()
        }

        sharedViewModel.user.observe(this.viewLifecycleOwner) {
            if (it == null) return@observe
            binding.apply {
                imgvAvatar.setUrl(it.photoUrl)
                tvInfoUserName.text = sharedViewModel.checkCurrentUser()?.displayName
                tvInfoPhoneNumber.text = it.contact.toString()
                tvInfoAddress.text = it.address
                tvInfoEmail.text = sharedViewModel.checkCurrentUser()?.email
            }
        }
    }
}