package com.dicoding.consumerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.consumerapp.databinding.UserListViewBinding
import com.squareup.picasso.Picasso

class UserFavAdapter: RecyclerView.Adapter<UserFavAdapter.UserFavViewHolder>() {

    var favUser = ArrayList<UserFavorite>()
        set(favUser){
            this.favUser.clear()
            this.favUser.addAll(favUser)
            notifyDataSetChanged()
        }

    inner class UserFavViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = UserListViewBinding.bind(itemView)
        fun bind(userFavorite: UserFavorite){
            binding.tvUsername.text = userFavorite.username
            binding.tvGithubLink.text = userFavorite.githubLink
            Picasso.with(itemView.context).load(userFavorite.avatarUrlLink).into(binding.avatarImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFavViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.user_list_view, parent, false)
        return UserFavViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserFavViewHolder, position: Int) {
        holder.bind(favUser[position])
    }

    override fun getItemCount(): Int = favUser.size

}