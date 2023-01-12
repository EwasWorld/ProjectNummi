package com.eywa.projectnummi.features.managePeople

import com.eywa.projectnummi.model.Person
import com.eywa.projectnummi.navigation.NummiNavRoute
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialogState

data class ManagePeopleState(
        val people: List<Person>? = null,
        val createDialogState: CreatePersonDialogState? = null,
        val manageItemDialogState: ManageItemDialogState<Person>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Person>? = null,
        val navigateInitiatedFor: NummiNavRoute? = null,
)
