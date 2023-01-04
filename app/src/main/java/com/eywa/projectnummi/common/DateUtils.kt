package com.eywa.projectnummi.common

import java.util.*

object DateUtils {
    fun currentDate(locale: Locale = Locale.getDefault()): Calendar =
            Calendar.getInstance(locale).apply {
                set(Calendar.HOUR_OF_DAY, 13) // 1pm
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
}
