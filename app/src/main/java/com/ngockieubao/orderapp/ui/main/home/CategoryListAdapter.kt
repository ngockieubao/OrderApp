package com.ngockieubao.orderapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.data.Category
import com.ngockieubao.orderapp.databinding.RcvCategoryBinding

class CategoryListAdapter(private val onItemClicked: (Category) -> Unit) :
    ListAdapter<Category, CategoryListAdapter.CategoryViewHolder>(DiffCallBack) {

    class CategoryViewHolder(private val binding: RcvCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category?) {
            if (item == null) return

            binding.apply {
                binding.item = item

                when (item.name) {
                    "Cơm" -> binding.imageViewLogoCategory.setImageResource(R.drawable.ic_rice)
                    "Phở" -> binding.imageViewLogoCategory.setImageResource(R.drawable.ic_pho)
                    "Lẩu" -> binding.imageViewLogoCategory.setImageResource(R.drawable.ic_hot_pot)
                    "Đồ ăn vặt" -> binding.imageViewLogoCategory.setImageResource(R.drawable.ic_snack)
                    "Đồ ăn nhanh" -> binding.imageViewLogoCategory.setImageResource(R.drawable.ic_fastfood)
                    "Đồ uống" -> binding.imageViewLogoCategory.setImageResource(R.drawable.ic_drink)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoryViewHolder(
            RcvCategoryBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
        holder.bind(item)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }
}