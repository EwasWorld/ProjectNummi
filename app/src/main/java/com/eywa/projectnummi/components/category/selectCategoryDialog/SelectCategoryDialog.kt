package com.eywa.projectnummi.components.category.selectCategoryDialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.components.category.CategoryItem
import com.eywa.projectnummi.components.category.selectCategoryDialog.SelectCategoryDialogIntent.*
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme

@Composable
fun SelectCategoryDialog(
        isShown: Boolean,
        state: SelectCategoryDialogState?,
        listener: (SelectCategoryDialogIntent) -> Unit,
) {
    NummiDialog(
            isShown = isShown && state != null,
            title = "Select a category",
            onCancelListener = { listener(Close) },
    ) {
        LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(NummiTheme.dimens.listItemSpacedBy),
        ) {
            item {
                CategoryItem(
                        category = null,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { listener(NoCategoryClicked) },
                )
            }

            items(state?.categories?.sortedBy { it.name } ?: listOf()) { item ->
                CategoryItem(
                        category = item,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { listener(CategoryClicked(item)) },
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
                            text = "New category",
                            color = NummiTheme.colors.appBackground.content,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SelectCategoryDialog_Preview() {
    NummiScreenPreviewWrapper {
        SelectCategoryDialog(
                isShown = true,
                state = SelectCategoryDialogState(CategoryProvider.basic),
                listener = {},
        )
    }
}
