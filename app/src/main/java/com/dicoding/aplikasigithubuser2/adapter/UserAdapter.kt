package com.dicoding.aplikasigithubuser2.adapter

import android.content.Intent
import android.os.Bundle
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

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    companion object {
        private val TAG = UserAdapter::class.java.simpleName
    }

    private val mData = ArrayList<UserListItem>()

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = UserListViewBinding.bind(itemView)
        fun bind(userListItem: UserListItem){
            binding.tvUsername.text = userListItem.username
            binding.tvGithubLink.text = userListItem.githubLink

            val extraUser = binding.tvUsername.text

            //using 3rd party library Picasso to display image from a link
            Picasso.with(itemView.context).load(userListItem.avatarUrlLink).into(binding.avatarImg)

            itemView.setOnClickListener{
                val viewUserDetailIntent = Intent(it.context, UserDetailActivity::class.java)
                Log.d(TAG, extraUser.toString()?: "null")
                viewUserDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, extraUser.toString())

                it.context.startActivity(viewUserDetailIntent)
            }
        }
    }

    fun setData(items: ArrayList<UserListItem>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.user_list_view, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size
}