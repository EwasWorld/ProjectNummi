package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.utils.ColorUtils

object CategoryProvider {
    val basic = listOf(
            "Entertainment" to 0f,
            "Groceries" to 0.33f,
            "Income" to 0.66f,
            "Work" to 0.8f,
            "Utilities" to 0.2f,
    ).mapIndexed { index, (name, hue) -> Category(index + 1, name, ColorUtils.asCategoryColor(hue)) }
}
