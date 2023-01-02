package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.common.ColorHelper
import com.eywa.projectnummi.model.Category

object CategoryProvider {
    val basic = listOf(
            "Entertainment" to 0f,
            "Groceries" to 0.33f,
            "Income" to 0.66f,
            "Work" to 0.8f,
    ).mapIndexed { index, (name, hue) -> Category(index, name, ColorHelper.asCategoryColor(hue)) }
}