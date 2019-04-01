package com.example.aospcontributors.presentation.contributors

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aospcontributors.R
import com.example.aospcontributors.databinding.ItemContributorBinding

class ContributorsListAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<ContributorsListAdapter.ContributorsListViewHolder>() {

    private var contributors: List<ContributorItem>? = null

    interface ItemClickListener {
        fun onLocationClick(location: String)
    }

    fun setContributors(contributors: List<ContributorItem>?) {
        this.contributors = contributors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributorsListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contributor, parent, false)
        return ContributorsListViewHolder(view)
    }

    override fun getItemCount() = contributors?.size ?: 0

    override fun onBindViewHolder(holder: ContributorsListViewHolder, position: Int) {
        contributors?.let { holder.bind(it[position], listener) }
    }

    class ContributorsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: ItemContributorBinding? = DataBindingUtil.bind(itemView)

        fun bind(contributor: ContributorItem, clickListener: ItemClickListener) {
            binding?.userName?.text = contributor.login

            val commitsCount = itemView.context.getString(R.string.commits_count, contributor.contributions)
            binding?.commitsCount?.text = commitsCount

            val imageView = binding?.userAvatar
            imageView?.let {
                Glide.with(itemView)
                        .load(contributor.avatarUrl)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .dontAnimate()
                        .into(imageView)
            }

            val userLocation = contributor.location
            if (!TextUtils.isEmpty(userLocation)) {
                binding?.locationContainer?.visibility = View.VISIBLE
                binding?.userLocation?.text = userLocation
                binding?.locationContainer?.setOnClickListener {
                    userLocation?.let {
                        clickListener.onLocationClick(userLocation)
                    }
                }
            } else {
                binding?.locationContainer?.visibility = View.GONE
            }
        }
    }
}
