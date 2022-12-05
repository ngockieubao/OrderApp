package com.ngockieubao.orderapp.ui.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngockieubao.orderapp.data.Order
import com.ngockieubao.orderapp.databinding.RcvListOrderBinding
import com.ngockieubao.orderapp.util.Utils

class ConfirmOrderListAdapter :
    ListAdapter<Order, ConfirmOrderListAdapter.ConfirmOrderViewHolder>(DiffCallBack) {

    inner class ConfirmOrderViewHolder(private val binding: RcvListOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order?) {
            if (item == null) return

            binding.apply {
                binding.item = item
                tvOrder.text = Utils.formatListOrder(item.name, item.price, item.quantity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmOrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ConfirmOrderViewHolder(
            RcvListOrderBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: ConfirmOrderViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem == newItem
            }
        }
    }
}