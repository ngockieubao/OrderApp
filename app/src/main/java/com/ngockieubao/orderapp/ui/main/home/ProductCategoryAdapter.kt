package com.ngockieubao.orderapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngockieubao.orderapp.data.Product
import com.ngockieubao.orderapp.databinding.RcvCategoryProductBinding
import com.ngockieubao.orderapp.util.Utils

class ProductCategoryAdapter(
    private val onItemClicked: (Product) -> Unit
) :
    ListAdapter<Product, ProductCategoryAdapter.ProductViewHolder>(DiffCallBack) {

    class ProductViewHolder(private val binding: RcvCategoryProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product?) {
            if (item == null) return

            binding.apply {
                binding.item = item
                binding.textViewProductPrice.text = Utils.formatPrice(item.price)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(
            RcvCategoryProductBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
        holder.bind(item)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}