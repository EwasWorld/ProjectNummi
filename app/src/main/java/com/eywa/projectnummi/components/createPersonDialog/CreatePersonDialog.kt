package com.eywa.projectnummi.components.createPersonDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.components.createPersonDialog.CreatePersonDialogIntent.*
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.components.NummiTextField

@Composable
fun CreatePersonDialog(
        isShown: Boolean,
        state: CreatePersonDialogState?,
        listener: (CreatePersonDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = "New person",
            okButtonText = "Create",
            onOkListener = { listener(Submit) },
            onCancelListener = { listener(Close) },
    ) {
        NummiTextField(
                text = state?.name ?: "",
                onTextChanged = { listener(NameChanged(it)) },
                label = "Name",
                placeholderText = "John Doe",
        )
    }
}

@Preview(
        device = Devices.PIXEL_4,
)
@Composable
fun CreatePersonDialog_Preview() {
    NummiScreenPreviewWrapper {
        CreatePersonDialog(
                isShown = true,
                state = CreatePersonDialogState(),
                listener = {},
        )
    }
}
