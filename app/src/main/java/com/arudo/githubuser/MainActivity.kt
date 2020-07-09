package com.arudo.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arudo.githubuser.adapter.ListAdapter
import com.arudo.githubuser.model.Lists
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListAdapter
    private lateinit var masterViewModel: MasterViewModel
    private var indexStatus = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result()
    }

    private fun result() {
        adapter = ListAdapter()
        adapter.notifyDataSetChanged()
        rvUser.adapter = adapter
        masterViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MasterViewModel::class.java
        )
        masterViewModel.getList().observe(this, Observer {
            if (it != null) {
                adapter.setData(it)
                statusText.setText(R.string.status_app)
                indexStatus = 1
                showLoading(false)
            }
        })
        masterViewModel.getStatusApp().observe(this, Observer {
            if (it != null) {
                statusText.text = it
                indexStatus = 0
                showLoading(false)
            }
        })
        adapter.setOnItemClickCallBack(object : ListAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: Lists) {
                selectedList(data)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchIcon: ImageView = searchView.findViewById(
            searchView.context.resources.getIdentifier(
                "android:id/search_mag_icon",
                null,
                null
            )
        )
        val searchPlate: View = searchView.findViewById(
            searchView.context.resources.getIdentifier(
                "android:id/search_plate",
                null,
                null
            )
        )
        val searchText: TextView = searchPlate.findViewById(
            searchPlate.context.resources.getIdentifier(
                "android:id/search_src_text",
                null,
                null
            )
        )
        val searchCloseIcon: ImageView = searchView.findViewById(
            searchView.context.resources.getIdentifier(
                "android:id/search_close_btn",
                null,
                null
            )
        )
        searchView.isIconifiedByDefault = false
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.queryHint)
        searchIcon.setImageResource(R.drawable.ic_search_black_24dp)
        searchText.setHintTextColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
        searchCloseIcon.setImageResource(R.drawable.ic_clear_24dp)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQuery("$query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.changeLocalization) {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }

    fun loadQuery(text: String) {
        showLoading(true)
        masterViewModel.setViewModel("https://api.github.com/search/users?q=${text}", "list")
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            statusText.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            rvUser.visibility = View.GONE
        } else {
            if (indexStatus == 1) {
                rvUser.visibility = View.VISIBLE
            } else if (indexStatus == 0) {
                statusText.visibility = View.VISIBLE
            }
            progressBar.visibility = View.GONE

        }
    }

    private fun selectedList(lists: Lists) {
        val moveUserIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveUserIntent.putExtra(DetailActivity.search, lists)
        startActivity(moveUserIntent)
    }
}