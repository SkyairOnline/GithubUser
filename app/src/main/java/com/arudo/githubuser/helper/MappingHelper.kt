package com.arudo.githubuser.helper

import android.database.Cursor
import com.arudo.githubuser.db.DatabaseContract
import com.arudo.githubuser.model.Lists

object MappingHelper {
    fun mapCursorToArrayList(listsCursor: Cursor?): ArrayList<Lists> {
        val lists = ArrayList<Lists>()
        listsCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.ListColumns._ID))
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.ListColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.ListColumns.AVATAR))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.ListColumns.URL))
                val urlApi = getString(getColumnIndexOrThrow(DatabaseContract.ListColumns.URLAPI))
                lists.add(Lists(id, username, avatar, url, urlApi))
            }
        }
        return lists
    }

    fun mapCursorToObject(listsCursor: Cursor?): Lists {
        var list = Lists()
        listsCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.ListColumns._ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.ListColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.ListColumns.AVATAR))
            val url = getString(getColumnIndexOrThrow(DatabaseContract.ListColumns.URL))
            val urlApi = getString(getColumnIndexOrThrow(DatabaseContract.ListColumns.URLAPI))
            list = Lists(id, username, avatar, url, urlApi)
        }
        return list
    }

}