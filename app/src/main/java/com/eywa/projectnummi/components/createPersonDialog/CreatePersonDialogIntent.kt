package com.eywa.projectnummi.components.createPersonDialog

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
