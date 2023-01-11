package com.eywa.projectnummi.components.deleteConfirmationDialog

sealed class DeleteConfirmationDialogIntent {
    object Ok : DeleteConfirmationDialogIntent()
    object Cancel : DeleteConfirmationDialogIntent()
}
