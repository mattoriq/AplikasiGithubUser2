package com.dicoding.aplikasigithubuser2.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.aplikasigithubuser2.fragments.FollowerFragment
import com.dicoding.aplikasigithubuser2.fragments.FollowingFragment

class FollowPageAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    var username: String? = null

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FollowingFragment.newInstance(username)
            1 -> fragment = FollowerFragment()
        }
        return fragment as Fragment
    }

}