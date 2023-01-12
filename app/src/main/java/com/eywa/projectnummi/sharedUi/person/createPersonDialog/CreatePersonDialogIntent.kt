package com.eywa.projectnummi.sharedUi.person.createPersonDialog

sealed class CreatePersonDialogIntent {
    data class NameChanged(val name: String) : CreatePersonDialogIntent()
    object Submit : CreatePersonDialogIntent()
    object Close : CreatePersonDialogIntent()

    fun updateState(state: CreatePersonDialogState): CreatePersonDialogState =
            when (this) {
                Close -> state
                Submit -> state
                is NameChanged -> state.copy(name = name)
            }
}
