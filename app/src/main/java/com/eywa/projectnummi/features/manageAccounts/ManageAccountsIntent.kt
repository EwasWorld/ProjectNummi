package com.eywa.projectnummi.features.manageAccounts

import com.eywa.projectnummi.components.createAccountDialog.CreateAccountDialogIntent

sealed class ManageAccountsIntent {
    object AddAccountClicked : ManageAccountsIntent()
    data class CreateAccountDialogAction(val action: CreateAccountDialogIntent) : ManageAccountsIntent()
}
