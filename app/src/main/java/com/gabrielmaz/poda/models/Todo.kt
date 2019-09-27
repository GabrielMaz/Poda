package com.gabrielmaz.poda.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.ZonedDateTime

@Parcelize
data class Todo(
    val id: Int,
    val description: String,
    var completed: Boolean,
    val dueDate: ZonedDateTime,
    val priority: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val categoryId: Int,
    val category: Category
) : Parcelable