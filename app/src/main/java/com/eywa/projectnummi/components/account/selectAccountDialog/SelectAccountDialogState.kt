package com.eywa.projectnummi.components.account.selectAccountDialog

import com.eywa.projectnummi.model.Account

data class SelectAccountDialogState(
        val accounts: List<Account> = listOf(),
)
