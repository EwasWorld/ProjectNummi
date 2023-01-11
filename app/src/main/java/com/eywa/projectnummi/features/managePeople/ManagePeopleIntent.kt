package com.eywa.projectnummi.features.managePeople

import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.components.person.createPersonDialog.CreatePersonDialogIntent
import com.eywa.projectnummi.model.Person

sealed class ManagePeopleIntent {
    data class PersonClicked(val person: Person) : ManagePeopleIntent()
    object AddPersonClicked : ManagePeopleIntent()
    data class CreatePersonDialogAction(val action: CreatePersonDialogIntent) : ManagePeopleIntent()
    data class ManageItemDialogAction(val action: ManageItemDialogIntent) : ManagePeopleIntent()
    data class DeleteConfirmationDialogAction(val action: DeleteConfirmationDialogIntent) : ManagePeopleIntent()
}
