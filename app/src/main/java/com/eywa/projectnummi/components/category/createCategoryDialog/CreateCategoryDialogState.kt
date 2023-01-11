package com.eywa.projectnummi.components.category.createCategoryDialog

import com.eywa.projectnummi.common.ColorUtils
import com.eywa.projectnummi.model.Category

data class CreateCategoryDialogState(
        val editing: Category? = null,
        val name: String = editing?.name ?: "",
        val hue: Float = editing?.color?.let { ColorUtils.colorToHuePercentage(it) } ?: 0.5f,
) {
    val isEditing = editing != null

    fun asCategory() = Category(
            id = editing?.id ?: 0,
            name = name,
            color = ColorUtils.asCategoryColor(hue),
    )
}
