package com.eywa.projectnummi.ui.components

import android.app.DatePickerDialog
import android.content.Context
import java.util.*

// TODO Set colour
object NummiDatePicker {
    fun createDialog(
            context: Context,
            date: Calendar,
            onDateChanged: (Calendar) -> Unit,
    ) = DatePickerDialog(
            context,
            { _, year, month, day ->
                onDateChanged(
                        (date.clone() as Calendar).apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DATE, day)
                        }
                )
            },
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DATE),
    )
}
