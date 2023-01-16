package com.eywa.projectnummi.sharedUi.deleteConfirmationDialog

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.eywa.projectnummi.model.HasName
import com.eywa.projectnummi.sharedUi.NummiDialog
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent.Cancel
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent.Ok

@Composable
fun <I : HasName> DeleteConfirmationDialog(
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
