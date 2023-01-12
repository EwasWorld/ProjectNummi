package com.eywa.projectnummi.sharedUi.deleteConfirmationDialog

sealed class DeleteConfirmationDialogIntent {
    object Ok : DeleteConfirmationDialogIntent()
    object Cancel : DeleteConfirmationDialogIntent()
}
