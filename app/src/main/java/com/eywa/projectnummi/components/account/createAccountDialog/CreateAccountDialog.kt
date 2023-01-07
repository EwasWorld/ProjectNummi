package com.eywa.projectnummi.components.account.createAccountDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.components.account.createAccountDialog.CreateAccountDialogIntent.*
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.components.NummiTextField

@Composable
fun CreateAccountDialog(
        isShown: Boolean,
        state: CreateAccountDialogState?,
        listener: (CreateAccountDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = "New account",
            okButtonText = "Create",
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
        device = Devices.PIXEL_4,
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
