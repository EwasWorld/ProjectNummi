package com.eywa.projectnummi.components.createCategoryDialog

import com.eywa.projectnummi.common.ColorHelper
import com.eywa.projectnummi.model.Category

data class CreateCategoryDialogState(
        val name: String = "",
        val hue: Float = 0.5f,
) {
    fun asCategory() = Category(
            id = 0,
            name = name,
            color = ColorHelper.asCategoryColor(hue),
    )
}
