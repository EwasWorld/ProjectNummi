package com.eywa.projectnummi.components.createCategoryDialog

sealed class CreateCategoryDialogIntent {
    data class NameChanged(val name: String) : CreateCategoryDialogIntent()
    data class HueChanged(val hue: Float) : CreateCategoryDialogIntent()
    object Submit : CreateCategoryDialogIntent()
    object Close : CreateCategoryDialogIntent()
}