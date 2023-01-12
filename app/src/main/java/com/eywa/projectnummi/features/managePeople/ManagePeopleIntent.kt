package com.eywa.projectnummi.features.managePeople

import com.eywa.projectnummi.model.Person
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialogIntent
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem

sealed class ManagePeopleIntent {
    data class TabClicked(val item: ManageTabSwitcherItem) : ManagePeopleIntent()
    object NavigationResolved : ManagePeopleIntent()
    data class PersonClicked(val person: Person) : ManagePeopleIntent()
    object AddPersonClicked : ManagePeopleIntent()
    data class CreatePersonDialogAction(val action: CreatePersonDialogIntent) : ManagePeopleIntent()
    data class ManageItemDialogAction(val action: ManageItemDialogIntent) : ManagePeopleIntent()
    data class DeleteConfirmationDialogAction(val action: DeleteConfirmationDialogIntent) : ManagePeopleIntent()
}
