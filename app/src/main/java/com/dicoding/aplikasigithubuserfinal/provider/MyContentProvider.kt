package com.dicoding.aplikasigithubuserfinal.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.aplikasigithubuserfinal.database.UserFavDao
import com.dicoding.aplikasigithubuserfinal.database.UserFavDatabase
import com.dicoding.aplikasigithubuserfinal.database.UserFavorite

class MyContentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.dicoding.aplikasigithubuserfinal"
        private const val TABLE_NAME = "user_fav"
        private const val DATA = 1
        private const val DATA_ITEM = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, DATA)
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", DATA_ITEM)
        }
    }

    private lateinit var userFavDao: UserFavDao

    override fun onCreate(): Boolean {
        userFavDao = UserFavDatabase.getDb(context as Context).userDao()
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (uriMatcher.match(uri)){
            DATA -> userFavDao.getAllAsCursor()
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        return 0
    }
}