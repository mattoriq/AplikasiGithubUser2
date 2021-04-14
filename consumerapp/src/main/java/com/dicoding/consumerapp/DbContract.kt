package com.dicoding.consumerapp

import android.net.Uri
import android.provider.BaseColumns

object DbContract {

    const val AUTHORITY = "com.dicoding.aplikasigithubuserfinal"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "user_fav"
            const val username = "username"
            const val githubLink = "githubLink"
            const val avatarUrlLink = "avatarUrlLink"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }

    }
}
