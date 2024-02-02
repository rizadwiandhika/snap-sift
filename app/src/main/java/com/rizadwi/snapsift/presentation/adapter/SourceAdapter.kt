package com.rizadwi.snapsift.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rizadwi.snapsift.databinding.ItemSourceBinding
import com.rizadwi.snapsift.model.Source
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SourceAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    Adapter<SourceAdapter.Holder>() {
    inner class Holder(val binding: ItemSourceBinding) : ViewHolder(binding.root)

    private var sourceList: List<Source> = listOf()


    private var onClicked: (Source) -> Unit = {}

    fun setSourceList(data: List<Source>) {
        sourceList = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(l: (Source) -> Unit) {
        onClicked = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return sourceList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val source = sourceList[position]
        with(holder.binding) {
            root.setOnClickListener {
                onClicked.invoke(source)
            }
            
            tvSource.text = source.name
        }
    }
}