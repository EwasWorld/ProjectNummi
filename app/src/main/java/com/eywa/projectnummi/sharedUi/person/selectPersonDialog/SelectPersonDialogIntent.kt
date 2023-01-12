package com.eywa.projectnummi.sharedUi.person.selectPersonDialog

import com.eywa.projectnummi.model.Person

sealed class SelectPersonDialogIntent {
    object CreateNew : SelectPersonDialogIntent()
    object Close : SelectPersonDialogIntent()
    data class PersonClicked(val person: Person?) : SelectPersonDialogIntent()
}
