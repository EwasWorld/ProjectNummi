package com.eywa.projectnummi.ui.components

import android.app.DatePickerDialog
import android.content.Context
import androidx.annotation.StyleRes
import java.util.*

object NummiDatePicker {
    fun createDialog(
            context: Context,
            date: Calendar,
            @StyleRes themeId: Int,
            onDateChanged: (Calendar) -> Unit,
    ) = DatePickerDialog(
            context,
            themeId,
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
