package com.eywa.projectnummi.components.person.createPersonDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.components.person.createPersonDialog.CreatePersonDialogIntent.*
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.components.NummiTextField

@Composable
fun CreatePersonDialog(
        isShown: Boolean,
        state: CreatePersonDialogState?,
        listener: (CreatePersonDialogIntent) -> Unit,
) {
    val isEditing = state?.editing != null
    NummiDialog(
            isShown = isShown && state != null,
            title = if (isEditing) "Editing " + state?.editing?.name else "New person",
            okButtonText = if (isEditing) "Update" else "Create",
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
        heightDp = 300,
        widthDp = 400,
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


@Preview(
        heightDp = 300,
        widthDp = 400,
)
@Composable
fun Edit_CreatePersonDialog_Preview() {
    NummiScreenPreviewWrapper {
        CreatePersonDialog(
                isShown = true,
                state = CreatePersonDialogState(
                        editing = PeopleProvider.basic[0],
                ),
                listener = {},
        )
    }
}
