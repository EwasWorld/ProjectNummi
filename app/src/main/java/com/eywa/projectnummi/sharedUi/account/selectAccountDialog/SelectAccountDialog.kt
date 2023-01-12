package com.eywa.projectnummi.sharedUi.account.selectAccountDialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.sharedUi.ItemList
import com.eywa.projectnummi.sharedUi.NummiDialog
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.account.AccountItem
import com.eywa.projectnummi.sharedUi.account.selectAccountDialog.SelectAccountDialogIntent.*

@Composable
fun SelectAccountDialog(
        isShown: Boolean,
        state: SelectAccountDialogState?,
        listener: (SelectAccountDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = "Select an account",
            onCancelListener = { listener(Close) },
    ) {
        ItemList(
                items = state?.accounts?.sortedBy { it.name },
                onNewItemClicked = { listener(CreateNew) },
                hasDefaultItem = true,
                newItemButtonText = "New account",
        ) {
            AccountItem(
                    account = it,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { listener(AccountClicked(it)) },
            )
        }
    }
}

@Preview
@Composable
fun SelectAccountDialog_Preview() {
    NummiScreenPreviewWrapper {
        SelectAccountDialog(
                isShown = true,
                state = SelectAccountDialogState(AccountProvider.basic),
                listener = {},
        )
    }
}
