package com.dicoding.aplikasigithubuserfinal.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.aplikasigithubuserfinal.fragments.FollowerFragment
import com.dicoding.aplikasigithubuserfinal.fragments.FollowingFragment

class FollowPageAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    var username: String? = null

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FollowingFragment.newInstance(username)
            1 -> fragment = FollowerFragment.newInstance(username)
        }
        return fragment as Fragment
    }

}