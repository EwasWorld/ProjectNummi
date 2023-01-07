package com.eywa.projectnummi.features.manageAccounts

import com.eywa.projectnummi.components.account.createAccountDialog.CreateAccountDialogIntent

sealed class ManageAccountsIntent {
    object AddAccountClicked : ManageAccountsIntent()
    data class CreateAccountDialogAction(val action: CreateAccountDialogIntent) : ManageAccountsIntent()
}
