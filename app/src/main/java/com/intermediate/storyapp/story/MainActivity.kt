package com.intermediate.storyapp.story

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.intermediate.storyapp.R
import com.intermediate.storyapp.adapter.LoadingAdapter
import com.intermediate.storyapp.adapter.StoryAdapter
import com.intermediate.storyapp.auth.AuthViewModel
import com.intermediate.storyapp.auth.LoginActivity
import com.intermediate.storyapp.databinding.ActivityMainBinding
import com.intermediate.storyapp.helper.ViewModelFactory
import com.intermediate.storyapp.maps.MapsActivity
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        onClick()

        binding.swipeRefresh.setOnRefreshListener {
            refresh()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                authViewModel.logout()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finishAffinity()
                true
            }
            R.id.setting-> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.maps-> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> false
        }
    }

    private fun refresh() {
        binding.swipeRefresh.isRefreshing = true
        storyAdapter.refresh()
        Timer().schedule(1000) {
            binding.swipeRefresh.isRefreshing = false
            binding.rvStory.smoothScrollToPosition(0)
        }
    }

    private fun onClick() {
        binding.addStory.setOnClickListener{
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setupView() {
        storyAdapter = StoryAdapter()

        mainViewModel.getUser().observe(this@MainActivity){ user ->
            if (user.isLogin){
                setStory()
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        with(binding.rvStory) {
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingAdapter{
                    storyAdapter.retry()
                })
        }
    }

    private fun setStory() {
        mainViewModel.getStory().observe(this@MainActivity) {
            storyAdapter.submitData(lifecycle, it)
            showLoading(false)
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)

        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }
        else {
            binding.progressBar.visibility = View.GONE
        }
    }
}