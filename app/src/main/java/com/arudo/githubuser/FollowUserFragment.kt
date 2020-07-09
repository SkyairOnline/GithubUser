package com.arudo.githubuser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arudo.githubuser.adapter.ListAdapter
import com.arudo.githubuser.model.Lists
import kotlinx.android.synthetic.main.fragment_followuser_list.*

class FollowUserFragment : Fragment() {
    private lateinit var adapter: ListAdapter
    private lateinit var followMasterViewModel: MasterViewModel
    private var followUrl = ""
    private var indexStatus = 0

    companion object {
        private const val follow = ""
        fun newInstance(urlFollow: String): FollowUserFragment {
            val fragment = FollowUserFragment()
            val bundle = Bundle()
            bundle.putString(follow, urlFollow)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followuser_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        result()
    }

    private fun result() {
        followUrl = arguments?.getString(follow, "") ?:""
        adapter = ListAdapter()
        adapter.notifyDataSetChanged()
        rvUser.adapter = adapter
        followMasterViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MasterViewModel::class.java
        )
        showLoading(true)
        followMasterViewModel.setViewModel(followUrl, "follow")
        followMasterViewModel.getList().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.setData(it)
                indexStatus = 1
                showLoading(false)
            }
        })
        followMasterViewModel.getStatusApp().observe(viewLifecycleOwner, Observer {
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

    private fun selectedList(list: Lists) {
        val moveUserIntent = Intent(activity, DetailActivity::class.java)
        moveUserIntent.putExtra(DetailActivity.search, list)
        startActivity(moveUserIntent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
            rvUser.visibility = View.GONE
            statusText.visibility = View.GONE
        } else {
            if (indexStatus == 0) {
                statusText.visibility = View.VISIBLE
            } else if (indexStatus == 1) {
                rvUser.visibility = View.VISIBLE
            }
            progressBar.visibility = View.GONE
        }
    }

}