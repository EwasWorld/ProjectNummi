package com.eywa.projectnummi.sharedUi.deleteConfirmationDialog

import com.eywa.projectnummi.model.NamedItem

data class DeleteConfirmationDialogState<I : NamedItem>(
        val item: I,
)
