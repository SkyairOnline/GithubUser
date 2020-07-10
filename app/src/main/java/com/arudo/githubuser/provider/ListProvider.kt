package com.arudo.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.arudo.githubuser.db.DatabaseContract.AUTHORITY
import com.arudo.githubuser.db.DatabaseContract.ListColumns.Companion.CONTENT_URI
import com.arudo.githubuser.db.DatabaseContract.ListColumns.Companion.TABLE_NAME
import com.arudo.githubuser.db.ListHelper

class ListProvider : ContentProvider() {
    companion object {
        private const val LIST = 1
        private const val LIST_ID = 2
        private lateinit var listHelper: ListHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, LIST)
            sUriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/#",
                LIST_ID
            )
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (LIST) {
            sUriMatcher.match(uri) -> listHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            LIST -> listHelper.queryAll()
            LIST_ID -> listHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        listHelper = ListHelper.getInstance(context as Context)
        listHelper.open()
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val updated: Int = when (LIST_ID) {
            sUriMatcher.match(uri) -> listHelper.update(
                uri.lastPathSegment.toString(),
                values
            )
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleted: Int = when (LIST_ID) {
            sUriMatcher.match(uri) -> listHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

}