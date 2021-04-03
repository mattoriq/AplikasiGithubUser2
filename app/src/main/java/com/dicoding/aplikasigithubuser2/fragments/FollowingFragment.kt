package com.dicoding.aplikasigithubuser2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aplikasigithubuser2.R
import com.dicoding.aplikasigithubuser2.apikey.Token
import com.dicoding.aplikasigithubuser2.adapter.FollowingAdapter
import com.dicoding.aplikasigithubuser2.databinding.FragmentFollowingBinding
import com.dicoding.aplikasigithubuser2.user.UserListItem
import com.dicoding.aplikasigithubuser2.viewmodel.FollowingViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingAdapter: FollowingAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var followingViewModel: FollowingViewModel
    //val listUsers = MutableLiveData<ArrayList<UserListItem>>()

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
        const val EXTRA_USER = "extra_user"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val mBundle = Bundle()
            mBundle.putString(EXTRA_USER, username)
            fragment.arguments = mBundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = FragmentFollowingBinding.inflate(inflater, container, false)
        return rootView.root
        //return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowingBinding.inflate(layoutInflater)

        recyclerView = binding.rvFollowing
        showRecyclerView()
        //binding.rvFollowing.layoutManager = LinearLayoutManager(requireActivity())

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        val username = arguments?.getString(EXTRA_USER)

        username?.let{
            Log.d(TAG,"setting User with username $it")
            followingViewModel.setUser(it)
        }

        followingViewModel.getUser().observe(viewLifecycleOwner, { userItems ->
            if (userItems != null){
                Log.d(TAG, "${(userItems.toString())} is here")
                followingAdapter.setData(userItems)
            }
        })

    }

    private fun showRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(activity)
        followingAdapter = FollowingAdapter()
        recyclerView.adapter = followingAdapter
        recyclerView.setHasFixedSize(true)
    }

    /*
    private fun setUser(username: String){
        val listItems = ArrayList<UserListItem>()

        val token = Token()
        val apiKey = token.getToken()
        val urlSearch = "https://api.github.com/users/${username}/following"

        Log.d(TAG, urlSearch)

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
                    val list = JSONArray(result)

                    for (i in 0 until list.length()){
                        val user = list.getJSONObject(i)
                        val userItems = UserListItem()
                        userItems.username = user.getString("login")
                        userItems.githubLink = user.getString("html_url")
                        userItems.avatarUrlLink = user.getString("avatar_url")
                        if (i == 0){
                            Log.d(TAG, "${userItems.username}, ${userItems.githubLink}")
                        }
                        listItems.add(userItems)
                    }
                    //listUsers.postValue(listItems)
                    Log.d(TAG, listItems[0].toString())
                    followingAdapter.setData(listItems)

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

     */

}