package com.eywa.projectnummi.features.manageCategories

import com.eywa.projectnummi.model.Category
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState

data class ManageCategoriesState(
        val categories: List<Category>? = null,
        val createDialogState: CreateCategoryDialogState? = null,
        val manageItemDialogState: ManageItemDialogState<Category>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Category>? = null,
)
