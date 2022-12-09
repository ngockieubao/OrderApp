package com.ngockieubao.orderapp.ui.main.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngockieubao.orderapp.base.OrderViewModelFactory
import com.ngockieubao.orderapp.data.Receipt
import com.ngockieubao.orderapp.databinding.FragmentReceiptBinding
import com.ngockieubao.orderapp.ui.main.OrderViewModel
import kotlinx.coroutines.launch

class ReceiptFragment : Fragment(), UpdateInterface {

    private lateinit var binding: FragmentReceiptBinding
    private val sharedViewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rcvHistory = binding.rcvPurchaseHistory
        val adapterHistory = ReceiptListAdapter(this@ReceiptFragment)

        rcvHistory.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rcvHistory.adapter = adapterHistory

        lifecycleScope.launch {
            sharedViewModel.getHistory()
        }
        sharedViewModel.listHistory.observe(this.viewLifecycleOwner) {
            if (it == null) return@observe
            adapterHistory.submitList(it)
        }
    }

    override fun clickToUpdateReceipt(item: Receipt?) {
        if (item == null) return
        val action = ReceiptFragmentDirections.actionReceiptFragmentToReceiptDetailFragment(item)
        this@ReceiptFragment.findNavController().navigate(action)
    }
}