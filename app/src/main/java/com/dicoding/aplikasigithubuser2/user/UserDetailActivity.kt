package com.dicoding.aplikasigithubuser2.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.aplikasigithubuser2.R
import com.dicoding.aplikasigithubuser2.databinding.ActivityUserDetailBinding
import com.dicoding.aplikasigithubuser2.adapter.FollowPageAdapter
import com.dicoding.aplikasigithubuser2.fragments.FollowingFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.picasso.Picasso
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class UserDetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TILES = intArrayOf(
            R.string.tab_following,
            R.string.tab_follower
        )
        const val EXTRA_USER = "extra_user"
        private val TAG = UserDetailActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserDetail()
    }

    private fun getUserDetail(){
        Log.d(TAG, "getUserDetail starting")
        binding.progressBar.visibility = View.VISIBLE

        val username = intent.getStringExtra(EXTRA_USER)
        val apiKey = "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"
        val urlUser = "https://api.github.com/users/${username}"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        Log.d(TAG, urlUser)

        client.get(urlUser, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                Log.d(TAG,"onSuccess starting...")
                binding.activityUserDetail.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)

                try {
                    val responseObject = JSONObject(result)

                    val aviLink = responseObject.getString("avatar_url")
                    val userName = responseObject.getString("login")
                    val fullName = responseObject.getString("name")
                    val company = responseObject.getString("company")
                    val location = responseObject.getString("location")
                    val repo = responseObject.getInt("public_repos")
                    val follower = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")

                    Picasso.with(this@UserDetailActivity).load(aviLink).into(binding.detailAvatar)
                    binding.detailUsername.text = userName
                    binding.detailName.text = checkNullString(fullName)
                    binding.detailCompany.text = getString(R.string.company, checkNullString(company))
                    binding.detailLocation.text = getString(R.string.location, checkNullString(location))
                    binding.detailRepo.text = getString(R.string.repo_totals, repo)
                    binding.detailFollowingNumber.text = getString(R.string.following, following)
                    binding.detailFollowerNumber.text = getString(R.string.follower, follower)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    e.printStackTrace()
                    Toast.makeText(this@UserDetailActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
                Toast.makeText(this@UserDetailActivity, error.message.toString(), Toast.LENGTH_SHORT).show()
            }

        })

        val followPageAdapter = FollowPageAdapter(this)
        Log.d(TAG,"$username to followPageAdapter")
        followPageAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = followPageAdapter
        val  tabs: TabLayout = binding.followTabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = resources.getString(TAB_TILES[position])
            } else {
                tab.text = resources.getString(TAB_TILES[position])
            }
        }.attach()
    }

    private fun checkNullString(str: String): String{
        if (str == "null"){
            return getString(R.string.not_specified)
        } else {
            return str
        }
    }

}