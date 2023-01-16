package com.eywa.projectnummi.model.objects

import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.database.DbColor
import com.eywa.projectnummi.database.category.DatabaseCategory
import com.eywa.projectnummi.model.HasNameAndId

// TODO Transaction.parentCategoryId feature
data class Category(
        val id: Int,
        val name: String,
        val color: Color,
) : HasNameAndId {
    constructor(
            dbCategory: DatabaseCategory,
    ) : this(
            dbCategory.id, dbCategory.name, dbCategory.color.value
    )

    fun asDbCategory() = DatabaseCategory(id, name, DbColor(color))

    override fun getItemName(): String = name

    override fun getItemId(): Int = id
}
