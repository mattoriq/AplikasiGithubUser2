package com.dicoding.aplikasigithubuserfinal.preference

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.aplikasigithubuserfinal.R
import com.dicoding.aplikasigithubuserfinal.databinding.ActivityPreferenceBinding

class PreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.preference)

        supportFragmentManager.beginTransaction().add(R.id.preference_activity, PreferenceFragment()).commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}