package com.eywa.projectnummi.features.manageCategories

import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogIntent

sealed class ManageCategoriesIntent {
    object AddCategoryClicked : ManageCategoriesIntent()
    data class CreateCategoryDialogAction(val action: CreateCategoryDialogIntent) : ManageCategoriesIntent()
}