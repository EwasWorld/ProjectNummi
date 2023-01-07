package com.eywa.projectnummi.components.person.selectPersonDialog

import com.eywa.projectnummi.model.Person

sealed class SelectPersonDialogIntent {
    object CreateNew : SelectPersonDialogIntent()
    object Close : SelectPersonDialogIntent()
    data class PersonClicked(val person: Person?) : SelectPersonDialogIntent()
}
