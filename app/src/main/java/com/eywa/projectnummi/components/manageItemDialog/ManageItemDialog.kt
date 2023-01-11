package com.eywa.projectnummi.components.manageItemDialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogIntent.*
import com.eywa.projectnummi.model.NamedItem
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme

@Composable
fun <I : NamedItem> ManageItemDialog(
        isShown: Boolean,
        state: ManageItemDialogState<I>?,
        listener: (ManageItemDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = state?.item?.getItemName() ?: "",
            onCancelListener = { listener(Close) },
    ) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                    text = "Edit",
                    color = NummiTheme.colors.appBackground.content,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                            .clickable { listener(EditClicked) }
                            .fillMaxWidth()
                            .padding(vertical = 15.dp)
            )
            Text(
                    text = "Delete",
                    color = NummiTheme.colors.appBackground.content,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                            .clickable { listener(DeleteClicked) }
                            .fillMaxWidth()
                            .padding(vertical = 15.dp)
            )
        }
    }
}

@Preview(
        heightDp = 300,
        widthDp = 400,
)
@Composable
fun ManageItemDialog_Preview() {
    NummiScreenPreviewWrapper {
        ManageItemDialog(
                isShown = true,
                state = ManageItemDialogState(AccountProvider.basic[2])
        ) {}
    }
}
