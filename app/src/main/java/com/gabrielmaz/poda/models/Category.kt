package com.gabrielmaz.poda.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.ZonedDateTime

@Parcelize
data class Category(
    val id: Int,
    val name: String,
    val color: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val photo: String
) : Parcelable