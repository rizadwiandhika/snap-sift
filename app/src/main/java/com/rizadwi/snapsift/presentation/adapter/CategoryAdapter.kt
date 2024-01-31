package com.rizadwi.snapsift.presentation.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rizadwi.snapsift.R
import com.rizadwi.snapsift.databinding.ItemCategoryLabelBinding
import javax.inject.Inject


class CategoryAdapter @Inject constructor() : Adapter<CategoryAdapter.Holder>() {
    inner class Holder(val binding: ItemCategoryLabelBinding) : ViewHolder(binding.root)

    private var categoryList: List<String> = listOf()
    private var activeIndex: Int = 0
    private var onClicked: (String, Int) -> Unit = { _, _ -> }

    fun setCategoryList(data: List<String>) {
        categoryList = data
        notifyDataSetChanged()
    }

    fun setActiveIndex(index: Int) {
        activeIndex = index
        notifyDataSetChanged()
    }

    fun setOnCategoryClickListener(l: (String, Int) -> Unit) {
        onClicked = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemCategoryLabelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val category = categoryList[position]
        with(holder.binding) {
            tvLabel.text = category

            if (position == activeIndex) {
                root.setBackgroundResource(R.drawable.rounded_md_blue)
                tvLabel.setTypeface(null, Typeface.BOLD)
            } else {
                root.setBackgroundResource(R.drawable.rounded_md_white)
                tvLabel.setTypeface(null, Typeface.NORMAL)
            }

            root.setOnClickListener {
                onClicked.invoke(category, position)
            }
        }
    }
}