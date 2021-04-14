package com.dicoding.aplikasigithubuserfinal.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aplikasigithubuserfinal.R
import com.dicoding.aplikasigithubuserfinal.database.UserFavorite
import com.dicoding.aplikasigithubuserfinal.databinding.UserListViewBinding
import com.dicoding.aplikasigithubuserfinal.user.UserDetailActivity
import com.squareup.picasso.Picasso

class UserFavAdapter: RecyclerView.Adapter<UserFavAdapter.UserFavViewHolder>() {

    companion object {
        private val TAG = UserFavAdapter::class.java.simpleName
    }

    private var favUser = emptyList<UserFavorite>()

    inner class UserFavViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = UserListViewBinding.bind(itemView)
        fun bind(userFavorite: UserFavorite){
            binding.tvUsername.text = userFavorite.username
            binding.tvGithubLink.text = userFavorite.githubLink
            Picasso.with(itemView.context).load(userFavorite.avatarUrlLink).into(binding.avatarImg)

            val extraUser = binding.tvUsername.text

            itemView.setOnClickListener{
                val viewUserDetailIntent = Intent(it.context, UserDetailActivity::class.java)
                Log.d(TAG, extraUser.toString())
                viewUserDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, extraUser.toString())

                it.context.startActivity(viewUserDetailIntent)
            }
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

    fun setData(userFav: List<UserFavorite>){
        this.favUser = userFav
        notifyDataSetChanged()
    }
}