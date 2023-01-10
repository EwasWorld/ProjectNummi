package com.eywa.projectnummi.components.category.selectCategoryDialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.components.ItemList
import com.eywa.projectnummi.components.category.CategoryItem
import com.eywa.projectnummi.components.category.selectCategoryDialog.SelectCategoryDialogIntent.*
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.ui.components.NummiDialog
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper

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
        ItemList(
                items = state?.categories?.sortedBy { it.name },
                onNewItemClicked = { listener(CreateNew) },
                hasDefaultItem = true,
                newItemButtonText = "New category",
        ) {
            CategoryItem(
                    category = it,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { listener(CategoryClicked(it)) },
            )
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
