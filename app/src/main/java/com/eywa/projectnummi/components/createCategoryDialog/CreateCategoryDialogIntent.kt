package com.eywa.projectnummi.components.createCategoryDialog

sealed class CreateCategoryDialogIntent {
    data class NameChanged(val name: String) : CreateCategoryDialogIntent()
    object Submit : CreateCategoryDialogIntent()
    object Close : CreateCategoryDialogIntent()
}