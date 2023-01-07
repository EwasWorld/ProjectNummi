package com.eywa.projectnummi.components.selectAccountDialog

import com.eywa.projectnummi.model.Account

data class SelectAccountDialogState(
        val accounts: List<Account> = listOf(),
)
