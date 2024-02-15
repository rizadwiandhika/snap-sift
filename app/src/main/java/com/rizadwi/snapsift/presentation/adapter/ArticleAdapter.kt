package com.rizadwi.snapsift.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.rizadwi.snapsift.R
import com.rizadwi.snapsift.databinding.ItemArticleBinding
import com.rizadwi.snapsift.model.Article
import com.rizadwi.snapsift.util.Clipper
import javax.inject.Inject


class ArticleAdapter @Inject constructor(private val clipper: Clipper) :
    Adapter<ArticleAdapter.Holder>() {
    inner class Holder(val binding: ItemArticleBinding) : ViewHolder(binding.root)

    private var articleList: MutableList<Article> = mutableListOf()
    private var onClicked: (Article) -> Unit = {}

    fun setArticleList(data: List<Article>) {
        articleList = data.toMutableList()
        notifyDataSetChanged()
    }

    fun appendArticleList(data: List<Article>) {
        if (data.isEmpty()) {
            return
        }

        val prevTotal = articleList.size
        articleList.addAll(data)

        notifyItemRangeInserted(prevTotal, data.size)
    }

    fun setOnItemClickListener(l: (Article) -> Unit) {
        onClicked = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val source = articleList[position]
        with(holder.binding) {
            root.setOnClickListener {
                onClicked.invoke(source)
            }

            Glide.with(root)
                .load(source.urlToImage)
                .centerCrop()
                .placeholder(R.drawable.pic_placeholder)
                .into(ivThumbnail)

            tvTitle.text = clipper.clip(source.title ?: "", MAX_TITLE_LENGTH)
            tvSource.text = source.source?.name ?: "Unknown source"
            tvAuthor.text = source.author
            tvDescription.text = clipper.clip(source.description ?: "", MAX_DESCRIPTION_LENGTH)
        }
    }

    companion object {
        const val MAX_TITLE_LENGTH = 48
        const val MAX_DESCRIPTION_LENGTH = 96
    }
}