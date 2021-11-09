package com.vdcodeassociate.fitme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vdcodeassociate.fitme.databinding.ProfileItemsBinding
import com.vdcodeassociate.fitme.model.profilemodel.ProfileItemsClass

class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.ProfileItemsViewHolder>() {

    // ViewHolder inner class
    inner class ProfileItemsViewHolder(val binding: ProfileItemsBinding): RecyclerView.ViewHolder(binding.root)

    // Diff Util call back
    private val differCallback = object : DiffUtil.ItemCallback<ProfileItemsClass>() {
        override fun areItemsTheSame(
            oldItem: ProfileItemsClass,
            newItem: ProfileItemsClass
        ): Boolean {
            return oldItem.component == newItem.component
        }

        override fun areContentsTheSame(
            oldItem: ProfileItemsClass,
            newItem: ProfileItemsClass
        ): Boolean {
            return oldItem == newItem
        }

    }

    // set differ
    val differ = AsyncListDiffer(this, differCallback)

    // Recycler Components
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileItemsViewHolder {
        return ProfileItemsViewHolder(
            ProfileItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ProfileItemsViewHolder, position: Int) {
        val itemsClass = differ.currentList[position]

        holder.binding.apply {

            profileItemsIcon.setBackgroundResource(itemsClass.image)

            profileItems.text = itemsClass.component

            root.setOnClickListener {
                onItemClickListener?.let { it(itemsClass) }
            }

        }

    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener: ((ProfileItemsClass) -> Unit)? = null

    fun setOnItemClickListener(listener: (ProfileItemsClass) -> Unit) {
        onItemClickListener = listener
    }

}