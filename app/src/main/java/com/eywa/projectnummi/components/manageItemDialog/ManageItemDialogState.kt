package com.eywa.projectnummi.components.manageItemDialog

import com.eywa.projectnummi.model.NamedItem

data class ManageItemDialogState<I : NamedItem>(
        val item: I,
)
