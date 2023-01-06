package com.eywa.projectnummi.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import com.eywa.projectnummi.common.DateUtils.asCalendar
import java.util.*

class DatabaseConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? = value.asCalendar()

    @TypeConverter
    fun toTimestamp(date: Calendar?): Long? = date?.timeInMillis

    @TypeConverter
    fun fromColor(value: Int?): DbColor? = value?.let { DbColor(Color(it)) }

    @TypeConverter
    fun toColor(color: DbColor?): Int? = color?.value?.toArgb()
}
