package com.gabrielmaz.poda.models

import org.threeten.bp.ZonedDateTime

class TodoListItem(
    var todo: Todo?,
    var header: ZonedDateTime?
)