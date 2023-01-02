package com.eywa.projectnummi.features.addTransactions.selectCategoryDialog

import com.eywa.projectnummi.model.Category

sealed class SelectCategoryDialogIntent {
    object CreateNew : SelectCategoryDialogIntent()
    object Close : SelectCategoryDialogIntent()
    object NoCategoryClicked : SelectCategoryDialogIntent()
    data class CategoryClicked(val category: Category) : SelectCategoryDialogIntent()
}