package com.dicoding.aplikasigithubuserfinal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aplikasigithubuserfinal.adapter.FollowingAdapter
import com.dicoding.aplikasigithubuserfinal.databinding.FragmentFollowerBinding
import com.dicoding.aplikasigithubuserfinal.user.UserListItem
import com.dicoding.aplikasigithubuserfinal.viewmodel.FollowerViewModel

class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private lateinit var followerAdapter: FollowingAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var followerViewModel: FollowerViewModel
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_USER = "extra_user"

        fun newInstance(username: String?): FollowerFragment {
            val fragment = FollowerFragment()
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
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvFollower
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        //Following Adapter borrowed because same function
        followerAdapter = FollowingAdapter()
        recyclerView.adapter = followerAdapter

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)
        val username = arguments?.getString(EXTRA_USER)

        username?.let{
            showLoading(true)
            followerViewModel.setUser(it)
        }

        followerViewModel.getUser().observe(viewLifecycleOwner, ::observeFollowerItems)
    }

    private fun observeFollowerItems(userItems: ArrayList<UserListItem>?){
        if (userItems != null){
            showLoading(false)
            followerAdapter.setData(userItems)
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