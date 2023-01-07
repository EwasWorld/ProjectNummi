package com.eywa.projectnummi.features.manageCategories

import com.eywa.projectnummi.components.category.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.model.Category

data class ManageCategoriesState(
        val categories: List<Category>? = null,
        val createDialogState: CreateCategoryDialogState? = null,
)
