package com.eywa.projectnummi.components.createAccountDialog

import com.eywa.projectnummi.model.Account

data class CreateAccountDialogState(
        val name: String = "",
        val type: String = "",
) {
    fun asAccount() = Account(
            id = 0,
            name = name,
            type = type.takeIf { it.isNotBlank() },
    )
}
