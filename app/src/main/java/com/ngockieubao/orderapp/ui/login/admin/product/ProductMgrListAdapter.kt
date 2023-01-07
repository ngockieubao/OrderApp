package com.ngockieubao.orderapp.ui.login.admin.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngockieubao.orderapp.data.Product
import com.ngockieubao.orderapp.databinding.RcvProductManagerBinding
import com.ngockieubao.orderapp.util.Utils

class ProductMgrListAdapter(
    private val onItemClicked: (Product) -> Unit,
    private val mgrInterface: MgrInterface
) :
    ListAdapter<Product, ProductMgrListAdapter.ProductMgrViewHolder>(DiffCallBack) {

    inner class ProductMgrViewHolder(private val binding: RcvProductManagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product?) {
            if (item == null) return

            binding.apply {
                binding.item = item
                tvItemPrice.text = Utils.formatPrice(item.price)
                constraintLayoutProductDetailContainer.setOnClickListener {
                    mgrInterface.clickToMgr(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductMgrViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductMgrViewHolder(
            RcvProductManagerBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: ProductMgrViewHolder, position: Int) {
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