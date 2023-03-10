package com.eywa.projectnummi.sharedUi.person.createPersonDialog

import com.eywa.projectnummi.model.objects.Person

data class CreatePersonDialogState(
        val editing: Person? = null,
        val name: String = editing?.name ?: "",
) {
    val isEditing = editing != null

    fun asPerson() = Person(
            id = editing?.id ?: 0,
            name = name,
    )
}
