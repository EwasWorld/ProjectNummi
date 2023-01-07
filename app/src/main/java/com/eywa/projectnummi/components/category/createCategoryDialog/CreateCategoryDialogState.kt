package com.eywa.projectnummi.components.category.createCategoryDialog

import com.eywa.projectnummi.common.ColorUtils
import com.eywa.projectnummi.model.Category

data class CreateCategoryDialogState(
        val name: String = "",
        val hue: Float = 0.5f,
) {
    fun asCategory() = Category(
            id = 0,
            name = name,
            color = ColorUtils.asCategoryColor(hue),
    )
}
