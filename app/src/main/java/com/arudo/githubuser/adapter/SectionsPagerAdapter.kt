package com.arudo.githubuser.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.arudo.githubuser.FollowUserFragment

class SectionsPagerAdapter (fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val fragmentDataList = ArrayList<String>()
    private val fragmentTitleList = ArrayList<String>()

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    override fun getItem(position: Int): Fragment {
        return FollowUserFragment.newInstance(fragmentDataList[position])
    }

    override fun getCount(): Int {
        return fragmentDataList.size
    }

    fun addFragmentData(url:String, title : String){
        fragmentDataList.add(url)
        fragmentTitleList.add(title)
    }
}