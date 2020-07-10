package com.arudo.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.arudo.githubuser.db.DatabaseContract.ListColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbListGithubUser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.ListColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.ListColumns.USERNAME} TEXT," +
                " ${DatabaseContract.ListColumns.AVATAR} TEXT," +
                " ${DatabaseContract.ListColumns.URL} TEXT," +
                " ${DatabaseContract.ListColumns.URLAPI} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}