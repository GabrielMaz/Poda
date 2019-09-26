package com.gabrielmaz.poda.models

import org.threeten.bp.ZonedDateTime

data class Todo(
    val id: Int,
    val description: String,
    val completed: Boolean,
    val dueDate: ZonedDateTime,
    val priority: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val categoryId: Int,
    val category: Category
)