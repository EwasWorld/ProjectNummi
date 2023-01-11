package com.eywa.projectnummi.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import com.eywa.projectnummi.common.DateTimeFormat
import com.eywa.projectnummi.common.DateUtils
import java.util.*

class DatabaseConverters {
    @TypeConverter
    fun fromTimestamp(value: String?): Calendar? = value?.let {
        val split = it.split("-").map { strVal -> strVal.toInt() }
        DateUtils.currentDate().apply {
            set(Calendar.YEAR, split[0])
            set(Calendar.MONTH, split[1] - 1)
            set(Calendar.DAY_OF_MONTH, split[2])
        }
    }

    @TypeConverter
    fun toTimestamp(date: Calendar?): String? = date?.let { DateTimeFormat.ISO_DATE.format(it) }

    @TypeConverter
    fun fromColor(value: Int?): DbColor? = value?.let { DbColor(Color(it)) }

    @TypeConverter
    fun toColor(color: DbColor?): Int? = color?.value?.toArgb()
}
