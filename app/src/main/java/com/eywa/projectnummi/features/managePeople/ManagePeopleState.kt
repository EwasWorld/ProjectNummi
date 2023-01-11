package com.eywa.projectnummi.features.managePeople

import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.components.person.createPersonDialog.CreatePersonDialogState
import com.eywa.projectnummi.model.Person

data class ManagePeopleState(
        val people: List<Person>? = null,
        val createDialogState: CreatePersonDialogState? = null,
        val manageItemDialogState: ManageItemDialogState<Person>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Person>? = null,
)
