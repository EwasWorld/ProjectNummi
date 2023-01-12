package com.eywa.projectnummi.sharedUi.manageItemDialog

import com.eywa.projectnummi.model.NamedItem

data class ManageItemDialogState<I : NamedItem>(
        val item: I,
        val options: List<ManageItemOption>,
)
