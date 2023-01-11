package com.eywa.projectnummi.components.manageItemDialog

interface ManageItemOption {
    val text: String
}

enum class ManageItemDefaultOption(override val text: String) : ManageItemOption {
    DELETE("Delete"),
    EDIT("Edit"),
}
