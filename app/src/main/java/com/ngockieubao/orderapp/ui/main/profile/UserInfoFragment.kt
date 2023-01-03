package com.ngockieubao.orderapp.ui.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.base.LoginViewModelFactory
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.databinding.FragmentUserInfoBinding
import com.ngockieubao.orderapp.ui.login.LoginViewModel
import com.ngockieubao.orderapp.ui.login.user.SignOutDialog
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import com.ngockieubao.orderapp.util.setUrl
import kotlinx.coroutines.launch

class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding
    private val mOrderViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(requireActivity().application)
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

        lifecycleScope.launch {
            mOrderViewModel.getUserInfo()
        }

        mOrderViewModel.user.observe(this.viewLifecycleOwner) {
            if (it == null) return@observe
            binding.apply {
                imgvAvatar.setUrl(it.photoUrl)
                tvInfoUserName.text = mOrderViewModel.checkCurrentUser()?.displayName
                tvInfoPhoneNumber.text = it.contact.toString()
                tvInfoAddress.text = it.address
                tvInfoEmail.text = mOrderViewModel.checkCurrentUser()?.email
            }
        }

        binding.apply {
            imgvRightChevronName.setOnClickListener {
//                navigateToEdtBtm()
            }
            imgvRightChevronPhoneNumber.setOnClickListener {
//                navigateToEdtBtm()
            }
            imgvRightChevronAddress.setOnClickListener {
//                navigateToEdtBtm()
            }
            imgvRightChevronEmail.setOnClickListener {
//                navigateToEdtBtm()
            }

        }

        binding.btnSignOut.setOnClickListener {
            dialog = SignOutDialog()
            dialog.show(childFragmentManager, "sign_out")
            loginViewModel.hasSignOut.observe(this.viewLifecycleOwner) {
                if (it == null) return@observe
                if (it == true) {
                    val action = UserInfoFragmentDirections.actionProfileFragmentToWelcomeFragment()
                    this@UserInfoFragment.findNavController().navigate(action)
                }
            }
        }
    }

    private fun navigateToEdtBtm() {
        val action = UserInfoFragmentDirections.actionProfileFragmentToEditProfileBtmFragment()
        this@UserInfoFragment.findNavController().navigate(action)
    }

    private fun updateUserInfo() {
        val bundle = arguments ?: return
        val args = UserInfoFragmentArgs.fromBundle(bundle)
        val value = args.value ?: return

        binding.apply {
//            imgvAvatar.setUrl(it.photoUrl)
            tvInfoUserName.text = value
            tvInfoPhoneNumber.text = value
            tvInfoAddress.text = value
            tvInfoEmail.text = value
        }
    }
}