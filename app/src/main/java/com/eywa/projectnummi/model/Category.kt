package com.eywa.projectnummi.model

import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.database.DbColor
import com.eywa.projectnummi.database.category.DatabaseCategory

// TODO Transaction.parentCategoryId feature
data class Category(
        val id: Int,
        val name: String,
        val color: Color,
) {
    constructor(
            dbCategory: DatabaseCategory,
    ) : this(
            dbCategory.id, dbCategory.name, dbCategory.color.value
    )

    fun asDbCategory() = DatabaseCategory(id, name, DbColor(color))
}
