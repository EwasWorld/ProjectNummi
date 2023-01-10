package com.eywa.projectnummi.components.person.selectPersonDialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.components.ItemList
import com.eywa.projectnummi.components.person.PersonItem
import com.eywa.projectnummi.components.person.selectPersonDialog.SelectPersonDialogIntent.*
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper

@Composable
fun SelectPersonDialog(
        isShown: Boolean,
        state: SelectPersonDialogState?,
        listener: (SelectPersonDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = "Select a person",
            onCancelListener = { listener(Close) },
    ) {
        ItemList(
                items = state?.people?.sortedBy { it.name },
                onNewItemClicked = { listener(CreateNew) },
                hasDefaultItem = true,
                newItemButtonText = "New person",
        ) {
            PersonItem(
                    person = it,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { listener(PersonClicked(it)) },
            )
        }
    }
}

@Preview
@Composable
fun SelectPersonDialog_Preview() {
    NummiScreenPreviewWrapper {
        SelectPersonDialog(
                isShown = true,
                state = SelectPersonDialogState(PeopleProvider.basic),
                listener = {},
        )
    }
}
