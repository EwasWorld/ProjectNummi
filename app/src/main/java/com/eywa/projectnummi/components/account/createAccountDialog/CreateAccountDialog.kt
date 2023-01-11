package com.eywa.projectnummi.components.account.createAccountDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.components.account.createAccountDialog.CreateAccountDialogIntent.*
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.components.NummiTextField

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
