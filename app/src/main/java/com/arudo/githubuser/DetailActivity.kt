package com.arudo.githubuser

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arudo.githubuser.adapter.SectionsPagerAdapter
import com.arudo.githubuser.model.Lists
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var userViewModel: MasterViewModel
    private var indexStatus = 0
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    companion object {
        const val search = "Search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        result()
    }

    private fun result() {
        val search = intent.getParcelableExtra(search) as Lists
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MasterViewModel::class.java
        )
        showLoading(true)
        userViewModel.setViewModel(search.urlApi, "user")
        userViewModel.getUser().observe(this, Observer {
            if (it != null) {
                txtUsername.text = it.username
                txtName.text = it.name
                Glide.with(this).load(it.avatar).centerCrop().transform(CircleCrop()).into(imgUser)
                txtCompany.text = it.company
                txtLocation.text = it.location
                txtRepository.text = it.repository.toString()
                txtFollower.text = it.follower.toString()
                txtFollowing.text = it.following.toString()
                txtBio.text = it.bio
                txtGist.text = it.gist.toString()
                txtUrl.text = it.url
                sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
                sectionsPagerAdapter.addFragmentData(
                    "https://api.github.com/users/${it.username}/following",
                    "Following"
                )
                sectionsPagerAdapter.addFragmentData(
                    "https://api.github.com/users/${it.username}/followers",
                    "Follower"
                )
                viewPager.adapter = sectionsPagerAdapter
                tabs.setupWithViewPager(viewPager)
                indexStatus = 1
                showLoading(false)
            }
        })
        userViewModel.getStatusApp().observe(this, Observer {
            if (it != null) {
                statusText.text = it
                indexStatus = 0
                showLoading(false)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            statusText.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            layoutDetail.visibility = View.GONE
        } else {
            if (indexStatus == 0) {
                statusText.visibility = View.VISIBLE
            } else if (indexStatus == 1) {
                layoutDetail.visibility = View.VISIBLE
            }
            progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.changeLocalization) {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }

}