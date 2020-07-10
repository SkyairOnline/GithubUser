package com.arudo.githubconsumer.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.arudo.githubconsumer.factory.StackFavoriteWidgetItemFactory

class StackFavoriteWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackFavoriteWidgetItemFactory(applicationContext, intent)
    }
}