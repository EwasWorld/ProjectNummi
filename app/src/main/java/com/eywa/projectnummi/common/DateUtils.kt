package com.eywa.projectnummi.common

import android.os.Build
import java.util.*

object DateUtils {
    fun currentDate(locale: Locale = Locale.getDefault()): Calendar =
            Calendar.getInstance(locale).apply {
                set(Calendar.HOUR_OF_DAY, 13) // 1pm
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

    fun Long?.asCalendar(): Calendar? = this?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Calendar.Builder().setInstant(it).build()
        }
        else {
            val date = Date(it)
            Calendar.getInstance(Locale.getDefault()).apply {
                @Suppress("DEPRECATION") // Will be removed when minSdk is increased
                set(
                        date.year + 1900,
                        date.month,
                        date.date,
                        date.hours,
                        date.minutes,
                        date.seconds,
                )
            }
        }
    }
}
