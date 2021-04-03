package com.dicoding.aplikasigithubuser2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aplikasigithubuser2.R
import com.dicoding.aplikasigithubuser2.adapter.FollowingAdapter
import com.dicoding.aplikasigithubuser2.databinding.FragmentFollowingBinding
import com.dicoding.aplikasigithubuser2.user.UserListItem
import com.dicoding.aplikasigithubuser2.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

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
        val rootView = inflater.inflate(R.layout.fragment_following, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.rv_following)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = FollowingAdapter()
        return rootView
        //return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowingBinding.inflate(layoutInflater)

        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.adapter = adapter

        followingViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[FollowingViewModel::class.java]
        val username = arguments?.getString(EXTRA_USER)
        Log.d(TAG,"$username onto viewModel")
        username?.let{
            Log.d(TAG,"$it near viewModel")
            followingViewModel.setUser(it)
        }

        followingViewModel.getUser().observe(viewLifecycleOwner, { userItems ->
            if (userItems != null){
                Log.d(TAG, "${(userItems.toString())} is here")
                adapter.setData(userItems)
            }
        })
    }

}