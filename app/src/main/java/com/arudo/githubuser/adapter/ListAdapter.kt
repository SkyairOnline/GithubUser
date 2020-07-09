package com.arudo.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arudo.githubuser.R
import com.arudo.githubuser.model.Lists
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.item_list_user.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>(){
    private lateinit var onItemClickCallBack:OnItemClickCallBack
    private val listData = ArrayList<Lists>()
    interface OnItemClickCallBack{
        fun onItemClicked(data: Lists)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun setData(items: ArrayList<Lists>) {
        listData.clear()
        listData.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        fun bind(lists: Lists) {
            with(itemView){
                txtName.text = lists.username
                txtUrl.text = lists.url
                Glide.with(itemView.context)
                    .load(lists.avatar)
                    .centerCrop()
                    .transform(CircleCrop())
                    .into(imgListUser)
                btnDetailLayout.setOnClickListener{
                    onItemClickCallBack.onItemClicked(lists)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(listViewHolder: ListViewHolder, position: Int) {
        listViewHolder.bind(listData[position])
    }

}