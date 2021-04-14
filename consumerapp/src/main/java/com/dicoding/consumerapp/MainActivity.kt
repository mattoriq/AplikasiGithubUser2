package com.dicoding.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerapp.DbContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.consumerapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserFavAdapter

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserFavAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUserFav.layoutManager = LinearLayoutManager(this)
        binding.rvUserFav.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavUserAsync()
        } else {
            savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)?.also { adapter.favUser = it }
        }

    }

    private fun loadFavUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val userFav = deferredUser.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (userFav.size > 0) {
                adapter.favUser = userFav
            } else {
                adapter.favUser = ArrayList()
                Snackbar.make(binding.rvUserFav, "No Favorite", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.favUser)
    }
}