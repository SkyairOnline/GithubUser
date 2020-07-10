package com.arudo.githubconsumer

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arudo.githubconsumer.adapter.ListAdapter
import com.arudo.githubconsumer.db.DatabaseContract.ListColumns.Companion.CONTENT_URI
import com.arudo.githubconsumer.helper.MappingHelper
import com.arudo.githubconsumer.model.Lists
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var listAdapter: ListAdapter
    private var lists = ArrayList<Lists>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val contentObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadDataAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, contentObserver)
        resultFavorite()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRestart() {
        super.onRestart()
        this.recreate()
    }

    private fun resultFavorite() {
        listAdapter = ListAdapter()
        listAdapter.notifyDataSetChanged()
        rvFavoriteUser.adapter = listAdapter
        loadDataAsync()
    }

    private fun loadDataAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                progressBar.visibility = View.VISIBLE
                rvFavoriteUser.visibility = View.GONE
                statusText.visibility = View.GONE
                val deferredLists = async(Dispatchers.IO) {
                    val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                lists = deferredLists.await()
                progressBar.visibility = View.GONE
                if (lists.size > 0) {
                    listAdapter.setData(lists)
                    rvFavoriteUser.visibility = View.VISIBLE
                    statusText.visibility = View.GONE
                } else {
                    rvFavoriteUser.visibility = View.GONE
                    statusText.visibility = View.VISIBLE
                }
                listAdapter.setOnItemClickCallBack(object : ListAdapter.OnItemClickCallBack {
                    override fun onItemClicked(data: Lists) {
                        selectedList(data)
                    }
                })
            } catch (ex: Exception) {
                showToastMessage(ex.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> startActivity(
                Intent(
                    this@FavoriteActivity,
                    SettingActivity::class.java
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }


    private fun selectedList(lists: Lists) {
        val moveUserIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        moveUserIntent.putExtra(DetailActivity.EXTRA_LIST, lists)
        startActivity(moveUserIntent)
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}