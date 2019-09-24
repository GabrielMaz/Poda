package com.gabrielmaz.poda.models

import org.threeten.bp.ZonedDateTime

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val avatar: String
)