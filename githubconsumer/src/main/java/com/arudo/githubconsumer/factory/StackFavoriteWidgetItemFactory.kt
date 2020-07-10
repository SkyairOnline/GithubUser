package com.arudo.githubconsumer.factory

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.arudo.githubconsumer.R
import com.arudo.githubconsumer.db.DatabaseContract.ListColumns.Companion.CONTENT_URI
import com.arudo.githubconsumer.helper.MappingHelper
import com.arudo.githubconsumer.model.Lists
import com.arudo.githubconsumer.receiver.StackFavoriteWidget
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class StackFavoriteWidgetItemFactory(private val context: Context, intent: Intent) :
    RemoteViewsService.RemoteViewsFactory {
    private var appWidgetId: Int = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )
    private var list = ArrayList<Lists>()

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDataSetChanged() {
        val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        list = MappingHelper.mapCursorToArrayList(cursor)
    }

    override fun hasStableIds(): Boolean = true

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.stack_favorite_item)
        val bitmap = Glide.with(context)
            .asBitmap()
            .load(list[position].avatar)
            .centerCrop()
            .transform(CircleCrop())
            .submit(100, 100)
            .get()
        remoteViews.setTextViewText(R.id.txtNameStackFavorite, list[position].username)
        remoteViews.setImageViewBitmap(R.id.imgUserStackFavorite, bitmap)
        val bundle = Bundle()
        bundle.putParcelable(StackFavoriteWidget.EXTRA_LIST, list[position])
        val fillIntent = Intent()
        fillIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        fillIntent.putExtras(bundle)
        remoteViews.setOnClickFillInIntent(R.id.stackItemLayout, fillIntent)
        return remoteViews
    }

    override fun getCount(): Int = list.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {

    }

}