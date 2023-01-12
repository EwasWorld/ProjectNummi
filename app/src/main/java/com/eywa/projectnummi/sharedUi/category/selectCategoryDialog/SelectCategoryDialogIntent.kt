package com.eywa.projectnummi.sharedUi.category.selectCategoryDialog

import com.eywa.projectnummi.model.Category

sealed class SelectCategoryDialogIntent {
    object CreateNew : SelectCategoryDialogIntent()
    object Close : SelectCategoryDialogIntent()
    data class CategoryClicked(val category: Category?) : SelectCategoryDialogIntent()
}
