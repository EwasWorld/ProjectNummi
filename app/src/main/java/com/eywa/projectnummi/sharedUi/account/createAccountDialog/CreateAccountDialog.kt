package com.eywa.projectnummi.sharedUi.account.createAccountDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.sharedUi.NummiDialog
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.NummiTextField
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialogIntent.*

@Composable
fun CreateAccountDialog(
        isShown: Boolean,
        state: CreateAccountDialogState?,
        listener: (CreateAccountDialogIntent) -> Unit,
) {
    val isEditing = state?.editing != null
    NummiDialog(
            isShown = isShown && state != null,
            title = if (isEditing) "Editing " + state?.editing?.name else "New account",
            okButtonText = if (isEditing) "Update" else "Create",
            onOkListener = { listener(Submit) },
            onCancelListener = { listener(Close) },
    ) {
        NummiTextField(
                text = state?.name ?: "",
                onTextChanged = { listener(NameChanged(it)) },
                label = "Name",
                placeholderText = "Nationwide main",
        )
        NummiTextField(
                text = state?.type ?: "",
                onTextChanged = { listener(TypeChanged(it)) },
                label = "Type",
                placeholderText = "Debit card",
        )
    }
}

@Preview(
        heightDp = 400,
        widthDp = 400,
)
@Composable
fun CreateAccountDialog_Preview() {
    NummiScreenPreviewWrapper {
        CreateAccountDialog(
                isShown = true,
                state = CreateAccountDialogState(),
                listener = {},
        )
    }
}

@Preview(
        heightDp = 400,
        widthDp = 400,
)
@Composable
fun Edit_CreateAccountDialog_Preview() {
    NummiScreenPreviewWrapper {
        CreateAccountDialog(
                isShown = true,
                state = CreateAccountDialogState(
                        editing = AccountProvider.basic[1],
                ),
                listener = {},
        )
    }
}
