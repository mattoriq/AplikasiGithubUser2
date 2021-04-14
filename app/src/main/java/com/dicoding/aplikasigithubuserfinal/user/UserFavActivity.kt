package com.dicoding.aplikasigithubuserfinal.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithubuserfinal.R
import com.dicoding.aplikasigithubuserfinal.adapter.UserFavAdapter
import com.dicoding.aplikasigithubuserfinal.databinding.ActivityUserFavBinding
import com.dicoding.aplikasigithubuserfinal.viewmodel.UserFavViewModel

class UserFavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavBinding
    private lateinit var adapter: UserFavAdapter
    private lateinit var mUserFavViewModel: UserFavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.fav_users)

        adapter = UserFavAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUserFav.layoutManager = LinearLayoutManager(this)
        binding.rvUserFav.adapter = adapter

        mUserFavViewModel = ViewModelProvider(this).get(UserFavViewModel::class.java)
        mUserFavViewModel.readDb.observe(this, { userFav ->
            adapter.setData(userFav)
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}