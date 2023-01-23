package com.eywa.projectnummi.features.manageCategories

import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem

sealed class ManageCategoriesIntent {
    data class TabClicked(val item: ManageTabSwitcherItem) : ManageCategoriesIntent()
    object NavigationResolved : ManageCategoriesIntent()
    data class CategoryClicked(val category: Category) : ManageCategoriesIntent()
    object AddCategoryClicked : ManageCategoriesIntent()
    data class CreateCategoryDialogAction(val action: CreateCategoryDialogIntent) : ManageCategoriesIntent()
    data class ManageItemDialogAction(val action: ManageItemDialogIntent) : ManageCategoriesIntent()
    data class DeleteConfirmationDialogAction(val action: DeleteConfirmationDialogIntent) : ManageCategoriesIntent()
    object DeleteThisAndSubCategories : ManageCategoriesIntent()
}
