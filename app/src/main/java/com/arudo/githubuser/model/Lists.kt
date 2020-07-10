package com.arudo.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lists (
    var id: Int = 0,
    var username: String? = null,
    var avatar: String? = null,
    var url: String? = null,
    var urlApi: String? = null
) : Parcelable