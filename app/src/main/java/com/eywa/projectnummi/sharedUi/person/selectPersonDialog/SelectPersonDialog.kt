package com.eywa.projectnummi.sharedUi.person.selectPersonDialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.sharedUi.ItemList
import com.eywa.projectnummi.sharedUi.NummiDialog
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.person.PersonItem
import com.eywa.projectnummi.sharedUi.person.selectPersonDialog.SelectPersonDialogIntent.*

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
