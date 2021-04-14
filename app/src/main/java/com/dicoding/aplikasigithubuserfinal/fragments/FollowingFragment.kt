package com.dicoding.aplikasigithubuserfinal.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aplikasigithubuserfinal.adapter.FollowingAdapter
import com.dicoding.aplikasigithubuserfinal.databinding.FragmentFollowingBinding
import com.dicoding.aplikasigithubuserfinal.user.UserListItem
import com.dicoding.aplikasigithubuserfinal.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private lateinit var followingAdapter: FollowingAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var followingViewModel: FollowingViewModel
    private val binding get() = _binding!!

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
        retainInstance = true
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvFollowing
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        followingAdapter = FollowingAdapter()
        recyclerView.adapter = followingAdapter

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        val username = arguments?.getString(EXTRA_USER)

        username?.let{
            Log.d(TAG,"setting User with username $it")
            showLoading(true)
            followingViewModel.setUser(it)
        }

        followingViewModel.getUser().observe(viewLifecycleOwner, ::observeFollowingItems)
    }

    private fun observeFollowingItems(userItems: ArrayList<UserListItem>?){
        if (userItems != null){
            Log.d(TAG, "${(userItems.toString())} is here")
            showLoading(false)
            followingAdapter.setData(userItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}