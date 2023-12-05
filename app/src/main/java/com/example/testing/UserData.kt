package com.example.testing

import android.media.Image
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    var id: String? = null,
    var fullname: String? = null,
    var username: String? = null,
    var password: String? = null,
    var email: String? = null,
    var noHp: String? = null,

    var image: String? = null
): Parcelable

