package com.eywa.projectnummi.features.manageCategories

import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.model.Category

data class ManageCategoriesState(
        val categories: List<Category> = listOf(),
        val createDialogState: CreateCategoryDialogState? = null,
)
