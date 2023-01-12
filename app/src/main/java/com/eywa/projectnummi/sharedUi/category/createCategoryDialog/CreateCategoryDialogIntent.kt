package com.eywa.projectnummi.sharedUi.category.createCategoryDialog

sealed class CreateCategoryDialogIntent {
    data class NameChanged(val name: String) : CreateCategoryDialogIntent()
    data class HueChanged(val hue: Float) : CreateCategoryDialogIntent()
    object Submit : CreateCategoryDialogIntent()
    object Close : CreateCategoryDialogIntent()

    fun updateState(state: CreateCategoryDialogState): CreateCategoryDialogState =
            when (this) {
                Close -> state
                Submit -> state
                is NameChanged -> state.copy(name = name)
                is HueChanged -> state.copy(hue = hue)
            }
}
