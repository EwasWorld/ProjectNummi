package com.eywa.projectnummi.features.manageCategories

import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.eywa.projectnummi.components.ItemList
import com.eywa.projectnummi.components.category.CategoryItem
import com.eywa.projectnummi.components.category.createCategoryDialog.CreateCategoryDialog
import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialog
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialog
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.*
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme

@Composable
fun ManageCategoriesScreen(
        viewModel: ManageCategoriesViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    ManageCategoriesScreen(state = state.value, listener = { viewModel.handle(it) })
}

@Composable
fun ManageCategoriesScreen(
        state: ManageCategoriesState,
        listener: (ManageCategoriesIntent) -> Unit,
) {
    val displayItems = state.categories?.sortedBy { it.name } ?: listOf()

    CreateCategoryDialog(
            isShown = true,
            state = state.createDialogState,
            listener = { listener(CreateCategoryDialogAction(it)) },
    )
    ManageItemDialog(
            isShown = state.deleteDialogState == null,
            state = state.manageItemDialogState,
            listener = { listener(ManageItemDialogAction(it)) },
    )
    DeleteConfirmationDialog(
            isShown = true,
            state = state.deleteDialogState,
            listener = { listener(DeleteConfirmationDialogAction(it)) },
    )

    Box(
            modifier = Modifier.fillMaxSize()
    ) {
        ItemList(
                items = displayItems,
                contentPadding = PaddingValues(NummiTheme.dimens.screenPadding),
        ) {
            CategoryItem(
                    category = it,
                    onClick = {
                        if (it != null) {
                            listener(CategoryClicked(it))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
            )
        }

        FloatingActionButton(
                backgroundColor = NummiTheme.colors.fab.main,
                contentColor = NummiTheme.colors.fab.content,
                onClick = { listener(AddCategoryClicked) },
                modifier = Modifier
                        .padding(NummiTheme.dimens.fabToScreenEdgePadding)
                        .align(Alignment.BottomEnd)
        ) {
            Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add category",
            )
        }
    }
}

@Preview
@Composable
fun ManageCategoriesScreen_Preview() {
    NummiScreenPreviewWrapper {
        ManageCategoriesScreen(
                state = ManageCategoriesState(
                        categories = CategoryProvider.basic,
                )
        ) {}
    }
}
