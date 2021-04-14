package com.dicoding.aplikasigithubuserfinal.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.aplikasigithubuserfinal.BuildConfig
import com.dicoding.aplikasigithubuserfinal.R
import com.dicoding.aplikasigithubuserfinal.databinding.ActivityUserDetailBinding
import com.dicoding.aplikasigithubuserfinal.adapter.FollowPageAdapter
import com.dicoding.aplikasigithubuserfinal.database.UserFavorite
import com.dicoding.aplikasigithubuserfinal.viewmodel.UserFavViewModel
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

    private var faved = false
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var mUserFavViewModel: UserFavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mUserFavViewModel = ViewModelProvider(this).get(UserFavViewModel::class.java)
        getUserDetail()
    }

    private fun getUserDetail(){
        Log.d(TAG, "getUserDetail starting")
        showLoading(true)
        val username = intent.getStringExtra(EXTRA_USER)
        supportActionBar?.title = getString(R.string.detail_title_bar, username)

        val apiKey = BuildConfig.GITHUB_TOKEN
        val urlUser = "https://api.github.com/users/${username}"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        Log.d(TAG, urlUser)

        client.get(urlUser, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                Log.d(TAG,"onSuccess starting...")
                showLoading(false)
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
                    val githubLink = responseObject.getString("html_url")

                    Picasso.with(this@UserDetailActivity).load(aviLink).into(binding.detailAvatar)
                    binding.detailUsername.text = userName
                    binding.detailName.text = checkNullString(fullName)
                    binding.detailCompany.text = getString(R.string.company, checkNullString(company))
                    binding.detailLocation.text = getString(R.string.location, checkNullString(location))
                    binding.detailRepo.text = getString(R.string.repo_totals, repo)
                    binding.detailFollowingNumber.text = getString(R.string.following, following)
                    binding.detailFollowerNumber.text = getString(R.string.follower, follower)

                    checkFav(userName)

                    binding.btnFav.setOnClickListener{
                        faved = !faved
                        setFavStatus(faved, userName, aviLink, githubLink)
                    }

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
        val tabs: TabLayout = binding.followTabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = resources.getString(TAB_TILES[position])
            } else {
                tab.text = resources.getString(TAB_TILES[position])
            }
        }.attach()
    }

    private fun checkNullString(str: String): String{
        return if (str == "null") getString(R.string.not_specified) else str
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.detailProgressBar.visibility = View.VISIBLE
            binding.activityUserDetail.visibility = View.GONE
        } else {
            binding.detailProgressBar.visibility = View.GONE
            binding.activityUserDetail.visibility = View.VISIBLE
        }
    }

    private fun setFavStatus(faved: Boolean, userName: String, aviLink: String, githubLink: String){
        if (faved) {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
            try {
                val userFaved = UserFavorite(0, userName, aviLink, githubLink)
                mUserFavViewModel.addFavUser(userFaved)
                Toast.makeText(this@UserDetailActivity, getString(R.string.add_fav_success), Toast.LENGTH_SHORT).show()
            } catch(e: Exception){
                e.printStackTrace()
                Toast.makeText(this@UserDetailActivity, getString(R.string.add_fav_failed), Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            try {
                mUserFavViewModel.deleteFavByName(userName)
                Toast.makeText(this@UserDetailActivity, getString(R.string.remove_fav_success), Toast.LENGTH_SHORT).show()
            } catch(e: Exception){
                e.printStackTrace()
                Toast.makeText(this@UserDetailActivity, getString(R.string.remove_fav_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFav(userName: String){
        mUserFavViewModel.readDb.observe(this, { userFav ->
            for (element in userFav){
                if (userName == element.username){
                    faved = true
                    binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            }
        })
    }

}