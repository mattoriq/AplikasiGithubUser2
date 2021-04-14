package com.dicoding.aplikasigithubuserfinal

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithubuserfinal.databinding.ActivityMainBinding
import com.dicoding.aplikasigithubuserfinal.viewmodel.MainViewModel
import com.dicoding.aplikasigithubuserfinal.adapter.UserAdapter
import com.dicoding.aplikasigithubuserfinal.preference.PreferenceActivity
import com.dicoding.aplikasigithubuserfinal.preference.PreferenceFragment
import com.dicoding.aplikasigithubuserfinal.user.UserFavActivity
import com.dicoding.aplikasigithubuserfinal.user.UserListItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        mainViewModel =  ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.getUser().observe(this, ::observeUserItems)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.username_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                mainViewModel.setUser(query)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    private fun observeUserItems(userItems: ArrayList<UserListItem>){
        if (userItems.isNotEmpty()){
            adapter.setData(userItems)
            showLoading(false)
        } else {
            Log.d(TAG, "null UserItems")
            Toast.makeText(this@MainActivity, getString(R.string.not_found), Toast.LENGTH_SHORT).show()
            showLoading(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_user_fav -> {
                val mIntent = Intent(this, UserFavActivity::class.java)
                startActivity(mIntent)
            }
            R.id.action_preference -> startActivity(Intent(this, PreferenceActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
            binding.rvUser.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvUser.visibility = View.VISIBLE
        }
    }

}