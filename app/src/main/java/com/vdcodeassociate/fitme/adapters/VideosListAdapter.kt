package com.vdcodeassociate.fitme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.digitalinclined.edugate.models.youtubemodel.Item
import com.vdcodeassociate.fitme.databinding.VideoContainerItemLayoutBinding

class VideosListAdapter : RecyclerView.Adapter<VideosListAdapter.QuizMainViewHolder>() {

    // Diff Util Call Back
    private val differCallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem.id.videoId == newItem.id.videoId
        }

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem == newItem
        }
    }

    // Differ Value Setup
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuizMainViewHolder {
        val binding = VideoContainerItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizMainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizMainViewHolder, position: Int) {

        // data from the dataclasses
        val data = differ.currentList[position]

        holder.binding.apply {

            // Image View
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
            requestOptions.centerCrop()
            if (data.snippet.thumbnails.high.url != null) {
                Glide.with(root)
                    .load(data.snippet.thumbnails.high.url)
                    .apply(requestOptions)
                    .into(videoThumbnail)
            }

            // quiz numbers
            videoTitle.text = data.snippet.title

            // on click listeners
            videoThumbnailLayout.setOnClickListener {
                onItemClickListener?.let { it(data) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    // Inner Class ViewHolder
    inner class QuizMainViewHolder(val binding: VideoContainerItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    // On click listener
    private var onItemClickListener: ((quizQuestions: Item) -> Unit)? = null

    fun setOnItemClickListener(listener: (quizQuestions: Item) -> Unit) {
        onItemClickListener = listener
    }
}