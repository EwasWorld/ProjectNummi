package com.eywa.projectnummi.features.addTransactions.selectCategoryDialog

import com.eywa.projectnummi.model.Category

data class SelectCategoryDialogState(
        val categories: List<Category> = listOf()
)