package com.eywa.projectnummi.components.createCategoryDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogIntent.*
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.components.NummiTextField

@Composable
fun CreateCategoryDialog(
        isShown: Boolean,
        state: CreateCategoryDialogState?,
        listener: (CreateCategoryDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = "New category",
            okButtonText = "Create",
            onOkListener = { listener(Submit) },
            onCancelListener = { listener(Close) },
    ) {
        NummiTextField(
                text = state?.name ?: "",
                onTextChanged = { listener(NameChanged(it)) },
                label = "Name",
                placeholderText = "Groceries",
        )
    }
}

@Preview(
        device = Devices.PIXEL_4,
)
@Composable
fun CreateCategoryDialog_Preview() {
    NummiScreenPreviewWrapper {
        CreateCategoryDialog(
                isShown = true,
                state = CreateCategoryDialogState(),
                listener = {},
        )
    }
}
