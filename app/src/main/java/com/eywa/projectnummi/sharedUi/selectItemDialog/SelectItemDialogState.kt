package com.eywa.projectnummi.sharedUi.selectItemDialog

import com.eywa.projectnummi.model.HasNameAndId

data class SelectItemDialogState<I : HasNameAndId>(
        val items: List<I> = emptyList(),
        val selectedItemIds: List<Int?> = emptyList(),
        val allowMultiSelect: Boolean = false,
) {
    fun isSelected(item: I?) = selectedItemIds.contains(item?.getItemId()).takeIf { allowMultiSelect }
}
