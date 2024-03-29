package com.ngockieubao.orderapp.ui.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngockieubao.orderapp.data.Order
import com.ngockieubao.orderapp.databinding.RcvOrderBinding
import com.ngockieubao.orderapp.util.Utils

class OrderListAdapter(
    private val onItemClicked: (Order) -> Unit,
    val updateInterface: UpdateInterface
) :
    ListAdapter<Order, OrderListAdapter.OrderViewHolder>(DiffCallBack) {

    inner class OrderViewHolder(private val binding: RcvOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order?) {
            if (item == null) return

            binding.apply {
                binding.item = item
                tvItemOrderPrice.text = Utils.formatPrice(item.price)
                textViewQuantity.text = item.quantity.toString()

                imageButtonDelete.setOnClickListener {
                    updateInterface.deleteItemOrder(item)
                }
                imageViewQuantityIncrease.setOnClickListener {
                    updateInterface.increaseQuantity(item.name, item.quantity)
                }
                imageViewQuantityDecrease.setOnClickListener {
                    updateInterface.decreaseQuantity(item.name, item.quantity)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrderViewHolder(
            RcvOrderBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
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