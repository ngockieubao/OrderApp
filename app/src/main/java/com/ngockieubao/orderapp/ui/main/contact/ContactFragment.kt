package com.ngockieubao.orderapp.ui.main.contact

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ngockieubao.orderapp.databinding.FragmentContactBinding
import com.ngockieubao.orderapp.util.Constants

class ContactFragment : Fragment() {

    private lateinit var binding: FragmentContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("IntentReset")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            imgvLogoAndroidPhone.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                val number = Constants.NUMBER_PHONE
                intent.data = Uri.parse("tel:$number")
                startActivity(intent)
            }
            imgvLogoYoutube.setOnClickListener {
                val url = Constants.URL_YOUTUBE
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
            btnFeedback.setOnClickListener {
                val action = ContactFragmentDirections.actionContactFragmentToFeedbackBtmSheetFragment()
                this@ContactFragment.findNavController().navigate(action)
            }
        }
    }
}