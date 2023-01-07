package com.eywa.projectnummi.components.createAccountDialog

sealed class CreateAccountDialogIntent {
    data class TypeChanged(val type: String) : CreateAccountDialogIntent()
    data class NameChanged(val name: String) : CreateAccountDialogIntent()
    object Submit : CreateAccountDialogIntent()
    object Close : CreateAccountDialogIntent()

    fun updateState(state: CreateAccountDialogState): CreateAccountDialogState =
            when (this) {
                Close -> state
                Submit -> state
                is TypeChanged -> state.copy(type = type)
                is NameChanged -> state.copy(name = name)
            }
}
