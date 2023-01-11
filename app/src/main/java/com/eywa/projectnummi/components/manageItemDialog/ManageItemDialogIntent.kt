package com.eywa.projectnummi.components.manageItemDialog

sealed class ManageItemDialogIntent {
    object EditClicked : ManageItemDialogIntent()
    object DeleteClicked : ManageItemDialogIntent()

    object Close : ManageItemDialogIntent()
}
