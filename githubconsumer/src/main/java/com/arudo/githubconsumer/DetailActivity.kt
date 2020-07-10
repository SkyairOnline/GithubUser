package com.arudo.githubconsumer

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arudo.githubconsumer.adapter.SectionsPagerAdapter
import com.arudo.githubconsumer.animation.FloatingActionIconAnimation
import com.arudo.githubconsumer.db.DatabaseContract.ListColumns.Companion.AVATAR
import com.arudo.githubconsumer.db.DatabaseContract.ListColumns.Companion.CONTENT_URI
import com.arudo.githubconsumer.db.DatabaseContract.ListColumns.Companion.URL
import com.arudo.githubconsumer.db.DatabaseContract.ListColumns.Companion.URLAPI
import com.arudo.githubconsumer.db.DatabaseContract.ListColumns.Companion.USERNAME
import com.arudo.githubconsumer.helper.MappingHelper
import com.arudo.githubconsumer.model.Lists
import com.arudo.githubconsumer.receiver.StackFavoriteWidget
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    private lateinit var userViewModel: MasterViewModel
    private var indexStatus = false
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var favoriteBtn: FloatingActionButton
    private lateinit var floatingActionIconAnimation: FloatingActionIconAnimation
    private var indexFavorite = false
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_LIST = "extra_list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        favoriteBtn = findViewById(R.id.favoriteBtn)
        floatingActionIconAnimation =
            FloatingActionIconAnimation(this)
        floatingActionIconAnimation.floatActionButton = favoriteBtn
        result()
    }

    private fun result() {
        var detail: Lists = intent.getParcelableExtra(EXTRA_LIST)!!
        indexFavorite = false
        val cursorSearch = contentResolver.query(CONTENT_URI, null, null, null, null)
        val listSearch = MappingHelper.mapCursorToArrayList(cursorSearch)
        for (listIndexSearch in listSearch) {
            if (detail.username.toString() == listIndexSearch.username) {
                indexFavorite = true
                uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + listIndexSearch.id)
                val cursor = contentResolver.query(uriWithId, null, null, null, null)
                if (cursor != null) {
                    detail = MappingHelper.mapCursorToObject(cursor)
                    cursor.close()
                }
                break
            }
        }
        floatingActionIconAnimation.icon(indexFavorite)
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MasterViewModel::class.java
        )
        showLoading(true)
        detail.urlApi?.let { userViewModel.setViewModel(it, "user") }
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
                indexStatus = true
                showLoading(false)
            }
        })
        userViewModel.getStatusApp().observe(this, Observer {
            if (it != null) {
                statusText.text = it
                indexStatus = false
                showLoading(false)
            }
        })
        favoriteBtn.setOnClickListener {
            floatingActionIconAnimation.animate(indexFavorite)
            if (!indexFavorite) {
                val values = ContentValues()
                values.put(USERNAME, detail.username)
                values.put(AVATAR, detail.avatar)
                values.put(URL, detail.url)
                values.put(URLAPI, detail.urlApi)
                contentResolver.insert(CONTENT_URI, values)
                showToastMessage("You just liked ${detail.username}. You can see it in favorite page.")
            } else if (indexFavorite) {
                contentResolver.delete(uriWithId, null, null)
                showToastMessage("You just removed ${detail.username}.")
            }
            val refreshWidget = Intent(this, StackFavoriteWidget::class.java)
            refreshWidget.action = "actionRefresh"
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                refreshWidget,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            pendingIntent.send()
            finish()
            indexFavorite = !indexFavorite
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            statusText.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            layoutDetail.visibility = View.GONE
        } else {
            if (!indexStatus) {
                statusText.visibility = View.VISIBLE
            } else if (indexStatus) {
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
        when (item.itemId) {
            R.id.setting -> startActivity(Intent(this@DetailActivity, SettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}