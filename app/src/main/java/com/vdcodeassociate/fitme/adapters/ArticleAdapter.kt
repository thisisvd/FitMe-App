package com.vdcodeassociate.fitme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.vdcodeassociate.fitme.databinding.NewsItemsBinding
import com.vdcodeassociate.fitme.utils.Utils
import com.vdcodeassociate.newsheadlines.kotlin.model.Article

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.NewsViewHolder>() {

    // ViewHolder inner class
    inner class NewsViewHolder(val binding: NewsItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Diff Util call back
    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    // set differ
    val differ = AsyncListDiffer(this, differCallback)

    // Recycler Components
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            NewsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        val article = differ.currentList[position]

        // Putting Image in ImageView
        val requestOptions = RequestOptions()
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.centerCrop()

        holder.binding.apply {
            if (article.urlToImage != null) {
                Glide.with(root)
                    .load(article.urlToImage)
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(vNewsImage)
            }
            newsTitle.text = article.title
            newsViews.text = article.source.name
            newsDate.text = article.author
            newsTime.text = Utils().DateToTimeFormat(article.publishedAt)

            root.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}