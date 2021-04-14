package com.dicoding.aplikasigithubuserfinal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasigithubuserfinal.database.UserFavDatabase
import com.dicoding.aplikasigithubuserfinal.database.UserFavRepository
import com.dicoding.aplikasigithubuserfinal.database.UserFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserFavViewModel(application: Application): AndroidViewModel(application) {

    private val repo: UserFavRepository
    val readDb: LiveData<List<UserFavorite>>

    init {
        val userFavDao = UserFavDatabase.getDb(application).userDao()
        repo = UserFavRepository(userFavDao)
        readDb = repo.readDb
    }

    fun addFavUser(userFavorite: UserFavorite){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addUserFav(userFavorite)
        }
    }

    fun deleteFavByName(username: String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteByName(username)
        }
    }

}