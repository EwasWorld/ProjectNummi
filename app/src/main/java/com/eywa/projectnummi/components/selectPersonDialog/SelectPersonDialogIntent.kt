package com.eywa.projectnummi.components.selectPersonDialog

import com.eywa.projectnummi.model.Person

sealed class SelectPersonDialogIntent {
    object CreateNew : SelectPersonDialogIntent()
    object Close : SelectPersonDialogIntent()
    data class PersonClicked(val person: Person?) : SelectPersonDialogIntent()
}
