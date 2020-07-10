package com.arudo.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.arudo.githubuser"
    const val SCHEME = "content"

    internal class ListColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "list"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val URL = "url"
            const val URLAPI = "urlApi"
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}