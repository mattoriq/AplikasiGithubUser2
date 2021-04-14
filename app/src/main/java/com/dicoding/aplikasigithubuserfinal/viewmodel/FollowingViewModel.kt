package com.dicoding.aplikasigithubuserfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithubuserfinal.BuildConfig
import com.dicoding.aplikasigithubuserfinal.user.UserListItem
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel: ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserListItem>>()

    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
    }

    fun setUser(username: String){
        val listItems = ArrayList<UserListItem>()

        val apiKey = BuildConfig.GITHUB_TOKEN
        val urlSearch = "https://api.github.com/users/${username}/following"

        //Log.d(TAG, urlSearch)

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")

        client.get(urlSearch, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray
            ) {
                try {
                    Log.d(TAG,"onSuccess starting...")
                    val result = String(responseBody)
                    //Log.d(TAG, result)
                    val list = JSONArray(result)

                    for (i in 0 until list.length()){
                        val user = list.getJSONObject(i)
                        val userItems = UserListItem()
                        userItems.username = user.getString("login")
                        userItems.githubLink = user.getString("html_url")
                        userItems.avatarUrlLink = user.getString("avatar_url")
                        listItems.add(userItems)
                    }
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<UserListItem>> = listUsers

}