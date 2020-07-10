package com.arudo.githubconsumer.receiver

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.arudo.githubconsumer.DetailActivity
import com.arudo.githubconsumer.R
import com.arudo.githubconsumer.model.Lists
import com.arudo.githubconsumer.service.StackFavoriteWidgetService

/**
 * Implementation of App Widget functionality.
 */
class StackFavoriteWidget : AppWidgetProvider() {
    companion object {
        const val ACTION_INTENT = "actionIntent"
        const val EXTRA_LIST = "extraList"
        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val serviceIntent = Intent(context, StackFavoriteWidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            serviceIntent.data = serviceIntent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val intent = Intent(context, StackFavoriteWidget::class.java)
            intent.action = ACTION_INTENT
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val remoteViews = RemoteViews(
                context.packageName,
                R.layout.stack_favorite_widget
            )
            remoteViews.setRemoteAdapter(R.id.stackFavoriteView, serviceIntent)
            remoteViews.setEmptyView(R.id.stackFavoriteView, R.id.emptyStackFavoriteView)
            remoteViews.setPendingIntentTemplate(R.id.stackFavoriteView, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stackFavoriteView)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_INTENT) {
            val intentDetail = Intent(context, DetailActivity::class.java)
            intentDetail.putExtra(
                DetailActivity.EXTRA_LIST, intent.getParcelableExtra(
                    EXTRA_LIST
                ) as Lists
            )
            intentDetail.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intentDetail)
        } else if (intent.action == "actionRefresh") {
            val remoteViews = RemoteViews(
                context.packageName,
                R.layout.stack_favorite_widget
            )
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context,
                    StackFavoriteWidget::class.java
                )
            )
            for (appWidgetId in appWidgetIds) {
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stackFavoriteView)
        }
    }
}

