package com.eywa.projectnummi.features.addTransactions.selectPersonDialog

import com.eywa.projectnummi.model.Person

sealed class SelectPersonDialogIntent {
    object CreateNew : SelectPersonDialogIntent()
    object Close : SelectPersonDialogIntent()
    data class PersonClicked(val person: Person) : SelectPersonDialogIntent()
}
