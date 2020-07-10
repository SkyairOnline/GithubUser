package com.arudo.githubuser.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.arudo.githubuser.factory.StackFavoriteWidgetItemFactory

class StackFavoriteWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackFavoriteWidgetItemFactory(applicationContext, intent)
    }
}