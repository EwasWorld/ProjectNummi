package com.eywa.projectnummi.model.objects

import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.database.DbColor
import com.eywa.projectnummi.database.category.DatabaseCategory
import com.eywa.projectnummi.database.category.FullDatabaseCategory
import com.eywa.projectnummi.model.HasNameAndId

data class Category(
        val id: Int,
        val name: String,
        val color: Color,
        val parent: Category? = null,
        val matchParentColor: Boolean = false,
) : HasNameAndId {
    constructor(
            dbCategory: FullDatabaseCategory,
    ) : this(
            dbCategory.category.id,
            dbCategory.category.name,
            dbCategory.category.color.value,
            dbCategory.parent?.let { Category(it) },
            dbCategory.category.matchParentColor,
    )

    fun asDbCategory() = DatabaseCategory(
            id = id,
            name = name,
            color = DbColor(color),
            parentCategoryId = parent?.id,
            matchParentColor = matchParentColor,
    )

    override fun getItemName(): String = name

    override fun getItemId(): Int = id

    fun hasParentWithId(id: Int): Boolean = parent?.id == id || parent?.hasParentWithId(id) == true
}
