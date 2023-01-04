package com.ngockieubao.orderapp.ui.main.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ngockieubao.orderapp.data.Banner
import com.ngockieubao.orderapp.databinding.LayoutImageSliderBinding
import com.ngockieubao.orderapp.util.setUrl

class BannerViewPager2Adapter : RecyclerView.Adapter<BannerViewPager2Adapter.BannerViewHolder>() {

    var listData = listOf<Banner>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class BannerViewHolder(private val binding: LayoutImageSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Banner?) {
            if (item == null) return
            binding.imgvSliderItem.setUrl(item.url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            LayoutImageSliderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}