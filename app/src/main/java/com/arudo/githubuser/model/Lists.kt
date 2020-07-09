package com.arudo.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lists (
    var username: String = "",
    var avatar: String = "",
    var url: String = "",
    var urlApi: String = ""
) : Parcelable