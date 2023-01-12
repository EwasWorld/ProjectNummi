package com.eywa.projectnummi.sharedUi.manageItemDialog

sealed class ManageItemDialogIntent {
    data class OptionClicked(val option: ManageItemOption) : ManageItemDialogIntent()

    object Close : ManageItemDialogIntent()
}
