package com.eywa.projectnummi.features.manageAccounts

import com.eywa.projectnummi.components.account.createAccountDialog.CreateAccountDialogState
import com.eywa.projectnummi.model.Account

data class ManageAccountsState(
        val accounts: List<Account>? = null,
        val createDialogState: CreateAccountDialogState? = null,
)
