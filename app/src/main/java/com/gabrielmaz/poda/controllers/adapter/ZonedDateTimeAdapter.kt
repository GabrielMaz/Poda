package com.gabrielmaz.poda.controllers.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ZonedDateTimeAdapter : TypeAdapter<ZonedDateTime>() {
    override fun write(out: JsonWriter, date: ZonedDateTime) {
        out.value(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(date))
    }

    override fun read(`in`: JsonReader) = ZonedDateTime.parse(`in`.nextString())
}
