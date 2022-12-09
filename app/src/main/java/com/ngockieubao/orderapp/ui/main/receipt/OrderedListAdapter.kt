package com.ngockieubao.orderapp.ui.main.receipt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngockieubao.orderapp.data.Order
import com.ngockieubao.orderapp.databinding.RcvOrderedBinding
import com.ngockieubao.orderapp.util.Utils

class OrderedListAdapter :
    ListAdapter<Order, OrderedListAdapter.OrderViewHolder>(DiffCallBack) {

    inner class OrderViewHolder(private val binding: RcvOrderedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order?) {
            if (item == null) return

            binding.apply {
                binding.item = item
                tvItemOrderPrice.text = Utils.formatPrice(item.price)
                tvItemOrderQuantity.text = Utils.formatQuantity(item.quantity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrderViewHolder(
            RcvOrderedBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
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