package com.eywa.projectnummi.sharedUi.category.createCategoryDialog

import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.utils.ColorUtils

data class CreateCategoryDialogState(
        val editing: Category? = null,
        val name: String = editing?.name ?: "",
        val hue: Float = editing?.color?.let { ColorUtils.colorToHuePercentage(it) } ?: 0.5f,
        val matchParentColor: Boolean = false,
        val categories: List<Category>?,
        val parentCategoryId: Int? = null,
        val selectParentCategoryDialogOpen: Boolean = false,
) {
    val isEditing = editing != null

    val parentCategory = parentCategoryId?.let { id -> categories?.find { it.id == id } }

    /**
     * Prevent a category from becoming a parent of itself
     */
    val filteredCategories by lazy {
        if (parentCategoryId == null) return@lazy categories
        val id = editing?.id ?: return@lazy categories
        categories?.filterNot { it.parent?.hasParentWithId(id) == true }
    }

    fun asCategory() = Category(
            id = editing?.id ?: 0,
            name = name,
            color = ColorUtils.asCategoryColor(hue),
            parent = parentCategory,
            matchParentColor = matchParentColor,
    )
}
