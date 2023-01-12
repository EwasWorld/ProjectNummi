package com.eywa.projectnummi.sharedUi.account.selectAccountDialog

import com.eywa.projectnummi.model.Account

sealed class SelectAccountDialogIntent {
    object CreateNew : SelectAccountDialogIntent()
    object Close : SelectAccountDialogIntent()
    data class AccountClicked(val account: Account?) : SelectAccountDialogIntent()
}