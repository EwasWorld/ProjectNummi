package com.eywa.projectnummi.components.createPersonDialog

import com.eywa.projectnummi.model.Person

data class CreatePersonDialogState(
        val name: String = "",
) {
    fun asPerson() = Person(
            id = 0,
            name = name,
    )
}
