package com.dicoding.consumerapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserFavorite(
    val username: String? = null,
    val avatarUrlLink: String? = null,
    val githubLink: String? = null
    ): Parcelable