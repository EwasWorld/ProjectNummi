package com.eywa.projectnummi.sharedUi.category.createCategoryDialog

import com.eywa.projectnummi.model.Category
import com.eywa.projectnummi.utils.ColorUtils

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
