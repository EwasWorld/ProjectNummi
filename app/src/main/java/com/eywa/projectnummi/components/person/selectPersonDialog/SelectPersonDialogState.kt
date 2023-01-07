package com.eywa.projectnummi.components.person.selectPersonDialog

import com.eywa.projectnummi.model.Person

data class SelectPersonDialogState(
        val people: List<Person> = listOf(),
)
