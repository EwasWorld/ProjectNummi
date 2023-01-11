package com.eywa.projectnummi.components.deleteConfirmationDialog

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogIntent.Cancel
import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogIntent.Ok
import com.eywa.projectnummi.model.NamedItem
import com.eywa.projectnummi.ui.components.NummiDialog

@Composable
fun <I : NamedItem> DeleteConfirmationDialog(
        isShown: Boolean,
        state: DeleteConfirmationDialogState<I>?,
        listener: (DeleteConfirmationDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = "Delete",
            okButtonText = "Ok",
            onOkListener = { listener(Ok) },
            onCancelListener = { listener(Cancel) },
    ) {
        Text(
                text = "Are you sure you want to delete " + state?.item?.getItemName(),
        )
    }
}
