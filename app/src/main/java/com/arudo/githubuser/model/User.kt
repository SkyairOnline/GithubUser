package com.arudo.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var username: String = "",
    var name: String = "",
    var avatar: String = "",
    var company: String = "",
    var location: String = "",
    var repository: Int = 0,
    var follower: Int = 0,
    var following: Int = 0,
    var bio: String = "",
    var url: String = "",
    var urlApi: String = "",
    var gist: Int = 0
) : Parcelable