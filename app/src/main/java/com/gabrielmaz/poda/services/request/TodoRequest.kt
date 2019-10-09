package com.gabrielmaz.poda.services.request

import org.threeten.bp.ZonedDateTime

data class TodoRequest(
    val priority: String,
    val description: String,
    val due_date: ZonedDateTime,
    val completed: Boolean,
    val category_id: Int
)