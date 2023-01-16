package com.eywa.projectnummi.sharedUi.selectItemDialog

import com.eywa.projectnummi.model.HasNameAndId
import com.eywa.projectnummi.utils.ListUtils.toggle

sealed class SelectItemDialogIntent {
    object CreateNew : SelectItemDialogIntent()
    object Close : SelectItemDialogIntent()
    data class ItemChosen<I : HasNameAndId>(val item: I?) : SelectItemDialogIntent()
    object Submit : SelectItemDialogIntent()

    data class ToggleItemSelected<I : HasNameAndId>(val item: I?) : InternalIntent()

    sealed class InternalIntent : SelectItemDialogIntent() {
        fun <I : HasNameAndId> update(state: SelectItemDialogState<I>): SelectItemDialogState<I> {
            return when (this) {
                is ToggleItemSelected<*> -> state.copy(selectedItemIds = state.selectedItemIds.toggle(item?.getItemId()))
            }
        }
    }
}
