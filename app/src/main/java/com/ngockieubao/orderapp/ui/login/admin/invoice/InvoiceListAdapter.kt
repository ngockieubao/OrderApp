package com.ngockieubao.orderapp.ui.login.admin.invoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngockieubao.orderapp.data.Receipt
import com.ngockieubao.orderapp.databinding.RcvListInvoiceBinding
import com.ngockieubao.orderapp.ui.main.receipt.UpdateInterface
import com.ngockieubao.orderapp.util.Utils

class InvoiceListAdapter(
    private val updateInterface: UpdateInterface
) : ListAdapter<Receipt, InvoiceListAdapter.InvoiceViewHolder>(DiffCallBack) {

    inner class InvoiceViewHolder(private val rcvListInvoiceBinding: RcvListInvoiceBinding) :
        RecyclerView.ViewHolder(rcvListInvoiceBinding.root) {
        fun bind(item: Receipt?) {
            if (item == null) return
            rcvListInvoiceBinding.apply {
                tvReceiptTimeOrderValue.text = item.time
                tvReceiptCodeValue.text = item.code
                tvReceiptTotalPurchaseValue.text = Utils.formatPrice(item.total)
                tvReceiptTypePurchaseValue.text = item.type
                lnReceiptContainer.setOnClickListener {
                    updateInterface.clickToUpdateReceipt(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return InvoiceViewHolder(
            RcvListInvoiceBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
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