package com.eywa.projectnummi.utils.providers

import java.util.*

object DateProvider {
    fun getDate(addDays: Int = 0): Calendar = Calendar.getInstance(Locale.getDefault())
            .apply { add(Calendar.DAY_OF_MONTH, addDays) }
}
