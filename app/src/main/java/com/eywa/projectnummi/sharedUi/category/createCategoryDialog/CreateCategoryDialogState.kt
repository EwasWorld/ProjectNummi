package com.eywa.projectnummi.sharedUi.category.createCategoryDialog

import com.eywa.projectnummi.database.DbColor
import com.eywa.projectnummi.database.category.DatabaseCategory
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.utils.ColorUtils

data class CreateCategoryDialogState(
        val categories: List<Category>?,

        val editing: Category? = null,
        val name: String = editing?.name ?: "",
        val hue: Float = editing?.dbColor?.let { ColorUtils.colorToHuePercentage(it) } ?: 0.5f,
        val matchParentColor: Boolean = editing?.matchParentColor ?: false,
        val parentCategoryId: Int? = editing?.parentIds?.firstOrNull(),

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
        categories?.filterNot { it.hasParentWithId(id) }
    }

    fun asDatabaseCategory() = DatabaseCategory(
            id = editing?.id ?: 0,
            name = name,
            color = DbColor(ColorUtils.asCategoryColor(hue)),
            parentCategoryId = parentCategoryId,
            matchParentColor = matchParentColor,
    )
}
