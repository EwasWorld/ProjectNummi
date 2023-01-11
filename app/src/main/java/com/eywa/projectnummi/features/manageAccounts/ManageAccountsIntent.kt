package com.eywa.projectnummi.features.manageAccounts

import com.eywa.projectnummi.components.account.createAccountDialog.CreateAccountDialogIntent
import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.model.Account

sealed class ManageAccountsIntent {
    data class AccountClicked(val account: Account) : ManageAccountsIntent()
    object AddAccountClicked : ManageAccountsIntent()
    data class CreateAccountDialogAction(val action: CreateAccountDialogIntent) : ManageAccountsIntent()
    data class ManageItemDialogAction(val action: ManageItemDialogIntent) : ManageAccountsIntent()
    data class DeleteConfirmationDialogAction(val action: DeleteConfirmationDialogIntent) : ManageAccountsIntent()
}
