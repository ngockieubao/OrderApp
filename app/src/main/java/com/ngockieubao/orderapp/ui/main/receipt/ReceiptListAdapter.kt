package com.ngockieubao.orderapp.ui.main.receipt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngockieubao.orderapp.data.Receipt
import com.ngockieubao.orderapp.databinding.RcvPurchaseHistoryBinding
import com.ngockieubao.orderapp.util.Utils

class ReceiptListAdapter(
    private val updateInterface: UpdateInterface,
) : ListAdapter<Receipt, ReceiptListAdapter.ReceiptViewHolder>(DiffCallBack) {

    inner class ReceiptViewHolder(private val binding: RcvPurchaseHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Receipt?) {
            if (item == null) return
            binding.item = item
            binding.tvReceiptTotalPurchaseValue.text = Utils.formatPrice(item.total)
            binding.tvReceiptTypePurchaseValue.text = item.type
            binding.tvOrderList.text = Utils.stringArrToList(item.receipts)
            binding.lnReceiptContainer.setOnClickListener {
                updateInterface.clickToUpdateReceipt(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReceiptViewHolder(
            RcvPurchaseHistoryBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Receipt>() {
            override fun areItemsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
                return oldItem == newItem
            }
        }
    }
}