package com.eywa.projectnummi.features.addTransactions.selectPersonDialog

import com.eywa.projectnummi.model.Person

data class SelectPersonDialogState(
        val people: List<Person> = listOf(),
)
