package com.gabrielmaz.poda.helpers

import org.threeten.bp.ZonedDateTime

fun ZonedDateTime.sameDayAs(anotherZonedDateTime: ZonedDateTime) =
    this.year == anotherZonedDateTime.year && this.dayOfYear == anotherZonedDateTime.dayOfYear