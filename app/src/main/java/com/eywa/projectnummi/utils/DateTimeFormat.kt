package com.eywa.projectnummi.utils

import java.text.SimpleDateFormat
import java.util.*

enum class DateTimeFormat(val pattern: String) {
    // 2021/01/01
    ISO_DATE("yyyy-MM-dd"),

    // 1 Jan 2021
    LONG_DATE_FULL_YEAR("d MMM yyyy"),

    // 1 Jan 21
    LONG_DATE("d MMM yy"),
    LONG_DAY_MONTH("d MMM"),

    // 01/01/21
    SHORT_DATE("dd/MM/yy"),

    TIME_24_HOUR("HH:mm"),
    TIME_12_HOUR("hh:mm a"),

    LONG_DATE_TIME("${LONG_DATE.pattern} ${TIME_24_HOUR.pattern}"),
    SHORT_DATE_TIME("${SHORT_DATE.pattern} ${TIME_24_HOUR.pattern}"),
    ;

    fun format(date: Calendar, locale: Locale = Locale.getDefault()): String =
            SimpleDateFormat(pattern, locale).format(date.time)
}
