package com.eywa.projectnummi.features.manageAccounts

import com.eywa.projectnummi.model.objects.Account
import com.eywa.projectnummi.navigation.NummiNavRoute
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialogState
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState

data class ManageAccountsState(
        val accounts: List<Account>? = null,
        val createDialogState: CreateAccountDialogState? = null,
        val manageItemDialogState: ManageItemDialogState<Account>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Account>? = null,
        val navigateInitiatedFor: NummiNavRoute? = null,
)
