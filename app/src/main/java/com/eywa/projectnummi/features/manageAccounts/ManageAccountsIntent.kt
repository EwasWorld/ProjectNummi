package com.eywa.projectnummi.features.manageAccounts

import com.eywa.projectnummi.model.Account
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialogIntent
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem

sealed class ManageAccountsIntent {
    data class TabClicked(val item: ManageTabSwitcherItem) : ManageAccountsIntent()
    object NavigationResolved : ManageAccountsIntent()
    data class AccountClicked(val account: Account) : ManageAccountsIntent()
    object AddAccountClicked : ManageAccountsIntent()
    data class CreateAccountDialogAction(val action: CreateAccountDialogIntent) : ManageAccountsIntent()
    data class ManageItemDialogAction(val action: ManageItemDialogIntent) : ManageAccountsIntent()
    data class DeleteConfirmationDialogAction(val action: DeleteConfirmationDialogIntent) : ManageAccountsIntent()
}
