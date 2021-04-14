package com.dicoding.aplikasigithubuserfinal.database

import androidx.lifecycle.LiveData

class UserFavRepository(private val userFavDao: UserFavDao) {
    val readDb: LiveData<List<UserFavorite>> = userFavDao.getAll()

    fun addUserFav(userFavorite: UserFavorite){
        userFavDao.insert(userFavorite)
    }

    fun deleteByName(username: String){
        userFavDao.deleteByName(username)
    }


}