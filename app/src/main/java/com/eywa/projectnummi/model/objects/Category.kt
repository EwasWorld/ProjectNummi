package com.eywa.projectnummi.model.objects

import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.database.DbColor
import com.eywa.projectnummi.database.category.DatabaseCategory
import com.eywa.projectnummi.database.category.FullDatabaseCategory
import com.eywa.projectnummi.model.HasNameAndId

data class Category(
        val id: Int,
        val name: String,

        /**
         * The color stored in the database under this category
         */
        val dbColor: Color,

        /**
         * True if this category should inherit its colour from its parent rather than using its own [dbColor]
         */
        val matchParentColor: Boolean = false,

        /**
         * The colour taking into account [matchParentColor]
         */
        val displayColor: Color = dbColor,
        /**
         * The ids of all parent categories (direct parent first, root last)
         */
        val parentIds: List<Int>? = null,
        /**
         * The names of all parent categories (direct parent first, root last)
         */
        val parentNames: List<String>? = null,
) : HasNameAndId {
    constructor(
            dbCategory: FullDatabaseCategory,
    ) : this(
            id = dbCategory.category.id,
            name = dbCategory.category.name,
            matchParentColor = dbCategory.category.matchParentColor,
            dbColor = dbCategory.category.color.value,
            displayColor = dbCategory.info?.color?.value ?: dbCategory.category.color.value,
            parentIds = dbCategory.info?.getParentsIds(),
            parentNames = dbCategory.info?.getAllNames(),
    )

    fun asDbCategory() = DatabaseCategory(
            id = id,
            name = name,
            color = DbColor(dbColor),
            parentCategoryId = parentIds?.first(),
            matchParentColor = matchParentColor,
    )

    override fun getItemName(): String = name

    override fun getItemId(): Int = id

    fun hasParentWithId(id: Int): Boolean = parentIds?.contains(id) ?: false
}
