package com.eywa.projectnummi.sharedUi.category.createCategoryDialog

import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectItemDialogIntent

sealed class CreateCategoryDialogIntent {
    object Submit : CreateCategoryDialogIntent()
    object Close : CreateCategoryDialogIntent()

    data class NameChanged(val name: String) : LocalChange()
    data class HueChanged(val hue: Float) : LocalChange()
    data class ParentCategoryChanged(val category: Category?) : LocalChange()
    object ToggleMatchParentColor : LocalChange()
    object OpenSelectParentCategoryDialog : LocalChange()
    object CloseSelectParentCategoryDialog : LocalChange()
    data class SelectParentCategoryDialogAction(val action: SelectItemDialogIntent) : LocalChange()

    sealed class LocalChange : CreateCategoryDialogIntent() {
        fun updateState(state: CreateCategoryDialogState): CreateCategoryDialogState =
                when (this) {
                    is NameChanged -> state.copy(name = name)
                    is HueChanged -> state.copy(hue = hue)
                    is ParentCategoryChanged -> state.copy(parentCategoryId = category?.id)
                    ToggleMatchParentColor -> state.copy(matchParentColor = !state.matchParentColor)
                    CloseSelectParentCategoryDialog -> state.copy(selectParentCategoryDialogOpen = false)
                    OpenSelectParentCategoryDialog -> state.copy(selectParentCategoryDialogOpen = true)
                    is SelectParentCategoryDialogAction -> when (action) {
                        SelectItemDialogIntent.Close -> state.copy(selectParentCategoryDialogOpen = false)
                        is SelectItemDialogIntent.ItemChosen<*> ->
                            state.copy(
                                    parentCategoryId = action.item?.getItemId(),
                                    selectParentCategoryDialogOpen = false,
                            )
                        else -> throw NotImplementedError()
                    }
                }
    }
}
