package com.dicoding.aplikasigithubuser2.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aplikasigithubuser2.R
import com.dicoding.aplikasigithubuser2.databinding.UserListViewBinding
import com.dicoding.aplikasigithubuser2.user.UserDetailActivity
import com.dicoding.aplikasigithubuser2.user.UserListItem
import com.squareup.picasso.Picasso

class FollowingAdapter: RecyclerView.Adapter<FollowingAdapter.FollowerViewHolder>() {

    companion object {
        private val TAG = FollowingAdapter::class.java.simpleName
    }

    private val mData = ArrayList<UserListItem>()

    fun setData(items: ArrayList<UserListItem>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class FollowerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = UserListViewBinding.bind(itemView)
        fun bind(userListItem: UserListItem){
            binding.tvUsername.text = userListItem.username
            binding.tvGithubLink.text = userListItem.githubLink
            //using 3rd party library Picasso to display image from a link
            Picasso.with(itemView.context).load(userListItem.avatarUrlLink).into(binding.avatarImg)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.following_list_view, parent, false)
        return FollowerViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

}