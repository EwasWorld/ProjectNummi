package com.eywa.projectnummi.features.manageCategories

import com.eywa.projectnummi.components.category.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.model.Category

data class ManageCategoriesState(
        val categories: List<Category>? = null,
        val createDialogState: CreateCategoryDialogState? = null,
        val manageItemDialogState: ManageItemDialogState<Category>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Category>? = null,
)
