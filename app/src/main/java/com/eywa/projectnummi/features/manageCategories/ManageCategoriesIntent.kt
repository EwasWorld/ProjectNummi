package com.eywa.projectnummi.features.manageCategories

import com.eywa.projectnummi.components.category.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.model.Category

sealed class ManageCategoriesIntent {
    data class CategoryClicked(val category: Category) : ManageCategoriesIntent()
    object AddCategoryClicked : ManageCategoriesIntent()
    data class CreateCategoryDialogAction(val action: CreateCategoryDialogIntent) : ManageCategoriesIntent()
    data class ManageItemDialogAction(val action: ManageItemDialogIntent) : ManageCategoriesIntent()
    data class DeleteConfirmationDialogAction(val action: DeleteConfirmationDialogIntent) : ManageCategoriesIntent()
}
