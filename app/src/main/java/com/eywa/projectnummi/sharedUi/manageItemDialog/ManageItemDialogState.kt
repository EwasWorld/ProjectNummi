package com.eywa.projectnummi.sharedUi.manageItemDialog

import com.eywa.projectnummi.model.HasName

data class ManageItemDialogState<I : HasName>(
        val item: I,
        val options: List<ManageItemOption>,
)
