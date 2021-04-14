package com.dicoding.consumerapp

import android.database.Cursor
import java.util.ArrayList

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<UserFavorite> {
        val userList = ArrayList<UserFavorite>()

        cursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DbContract.UserColumns.username))
                val githubLink = getString(getColumnIndexOrThrow(DbContract.UserColumns.githubLink))
                val aviLink = getString(getColumnIndexOrThrow(DbContract.UserColumns.avatarUrlLink))
                userList.add(UserFavorite(username, aviLink, githubLink))
            }
        }
        return userList
    }

}
