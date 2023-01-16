package com.eywa.projectnummi.sharedUi.deleteConfirmationDialog

import com.eywa.projectnummi.model.HasName

data class DeleteConfirmationDialogState<I : HasName>(
        val item: I,
)
