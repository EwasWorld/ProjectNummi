package com.eywa.projectnummi.features.managePeople

import com.eywa.projectnummi.components.person.createPersonDialog.CreatePersonDialogIntent

sealed class ManagePeopleIntent {
    object AddPersonClicked : ManagePeopleIntent()
    data class CreatePersonDialogAction(val action: CreatePersonDialogIntent) : ManagePeopleIntent()
}
