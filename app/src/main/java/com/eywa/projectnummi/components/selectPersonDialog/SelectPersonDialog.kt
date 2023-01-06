package com.eywa.projectnummi.components.selectPersonDialog

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.components.selectPersonDialog.SelectPersonDialogIntent.*
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme

@OptIn(ExperimentalMaterialApi::class)
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
        LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            item {
                PersonRow(
                        name = "Default",
                        onClick = { listener(PersonClicked(null)) }
                )
            }

            items(state?.people?.sortedBy { it.name } ?: listOf()) { item ->
                PersonRow(
                        name = item.name,
                        onClick = { listener(PersonClicked(item)) }
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
                            text = "New person",
                            color = NummiTheme.colors.appBackground.content,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PersonRow(
        name: String,
        onClick: () -> Unit,
) {
    Surface(
            color = Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
    ) {
        Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
        ) {
            Text(
                    text = name,
                    color = NummiTheme.colors.appBackground.content,
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
