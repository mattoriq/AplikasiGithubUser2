package com.dicoding.aplikasigithubuserfinal.database

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_fav")
data class UserFavorite(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val username: String,
    val avatarUrlLink: String,
    val githubLink: String
    )
