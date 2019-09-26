package com.gabrielmaz.poda.models

import org.threeten.bp.ZonedDateTime

data class Category(
    val id: Int,
    val name: String,
    val color: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val photo: String
)