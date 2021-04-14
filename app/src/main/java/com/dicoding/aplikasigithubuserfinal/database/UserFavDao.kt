package com.dicoding.aplikasigithubuserfinal.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserFavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserFavorite)

    @Delete
    fun delete(user: UserFavorite)

    @Query("SELECT * FROM user_fav")
    fun getAll(): LiveData<List<UserFavorite>>

    @Query("SELECT * FROM user_fav")
    fun getAllAsCursor(): Cursor

    @Query("DELETE FROM user_fav WHERE username LIKE :user_name")
    fun deleteByName(user_name: String)

}