package com.dicoding.aplikasigithubuser2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithubuser2.apikey.Token
import com.dicoding.aplikasigithubuser2.fragments.FollowingFragment
import com.dicoding.aplikasigithubuser2.user.UserListItem
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel: ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    val listUsers = MutableLiveData<ArrayList<UserListItem>>()

    fun setUser(username: String){
        val listItems = ArrayList<UserListItem>()

        val token = Token()
        val apiKey = token.getToken()
        val urlSearch = "https://api.github.com/search/users?q=${username}"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
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
                    Log.d(TAG, result)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()){
                        val user = list.getJSONObject(i)
                        val userItems = UserListItem()
                        userItems.username = user.getString("login")
                        userItems.githubLink = user.getString("html_url")
                        userItems.avatarUrlLink = user.getString("avatar_url")
                        listItems.add(userItems)
                    }
                    Log.d(TAG, listItems[0].toString())
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

    fun getUser(): LiveData<ArrayList<UserListItem>>{
        return listUsers
    }

}