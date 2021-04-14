package com.dicoding.aplikasigithubuserfinal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserFavorite::class], version = 1, exportSchema = false)
abstract class UserFavDatabase: RoomDatabase() {
    abstract fun userDao(): UserFavDao

    companion object {
        @Volatile
        private var INSTANCE: UserFavDatabase? = null

        fun getDb(context: Context): UserFavDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserFavDatabase::class.java,
                        "user_fav_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}