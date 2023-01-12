package com.eywa.projectnummi.sharedUi.account.createAccountDialog

import com.eywa.projectnummi.model.Account

data class CreateAccountDialogState(
        val editing: Account? = null,
        val name: String = editing?.name ?: "",
        val type: String = editing?.type ?: "",
) {
    val isEditing = editing != null

    fun asAccount() = Account(
            id = editing?.id ?: 0,
            name = name,
            type = type.takeIf { it.isNotBlank() },
    )
}
