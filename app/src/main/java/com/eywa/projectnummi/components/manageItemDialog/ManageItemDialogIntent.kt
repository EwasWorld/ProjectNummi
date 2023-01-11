package com.eywa.projectnummi.components.manageItemDialog

sealed class ManageItemDialogIntent {
    data class OptionClicked(val option: ManageItemOption) : ManageItemDialogIntent()

    object Close : ManageItemDialogIntent()
}
