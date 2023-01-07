package com.eywa.projectnummi.components.selectAccountDialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.components.selectAccountDialog.SelectAccountDialogIntent.*
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme

@Composable
fun SelectAccountDialog(
        isShown: Boolean,
        state: SelectAccountDialogState?,
        listener: (SelectAccountDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = "Select an account",
            onCancelListener = { listener(Close) },
    ) {
        LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            item {
                AccountRow(
                        name = "Default",
                        type = null,
                        onClick = { listener(AccountClicked(null)) }
                )
            }

            items(state?.accounts?.sortedBy { it.name } ?: listOf()) { item ->
                AccountRow(
                        name = item.name,
                        type = item.type,
                        onClick = { listener(AccountClicked(item)) }
                )
            }

            item {
                Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .clickable { listener(CreateNew) }
                ) {
                    Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = NummiTheme.colors.appBackground.content,
                    )
                    Text(
                            text = "New account",
                            color = NummiTheme.colors.appBackground.content,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AccountRow(
        name: String,
        type: String?,
        onClick: () -> Unit,
) {
    Surface(
            color = Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
        ) {
            Text(
                    text = name,
                    color = NummiTheme.colors.appBackground.content,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (type != null) {
                Text(
                        text = type,
                        color = NummiTheme.colors.appBackground.content,
                        fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

@Preview
@Composable
fun SelectAccountDialog_Preview() {
    NummiScreenPreviewWrapper {
        SelectAccountDialog(
                isShown = true,
                state = SelectAccountDialogState(AccountProvider.basic),
                listener = {},
        )
    }
}
