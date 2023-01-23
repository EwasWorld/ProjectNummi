package com.eywa.projectnummi.features.manageCategories

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.*
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.sharedUi.*
import com.eywa.projectnummi.sharedUi.category.CategoryItem
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialog
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialog
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialog
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem
import com.eywa.projectnummi.theme.NummiTheme
import kotlinx.coroutines.launch

@Composable
fun ManageCategoriesScreen(
        navController: NavController,
        viewModel: ManageCategoriesViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value.navigateInitiatedFor) {
        launch {
            state.value.navigateInitiatedFor?.let {
                viewModel.handle(NavigationResolved)
                navigateToManageTab(it, navController)
            }
        }
    }

    ManageCategoriesScreen(state = state.value, listener = { viewModel.handle(it) })
}

@Composable
fun ManageCategoriesScreen(
        state: ManageCategoriesState,
        listener: (ManageCategoriesIntent) -> Unit,
) {
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
            isShown = state.deleteWithSubCategoriesState == null,
            state = state.deleteDialogState,
            listener = { listener(DeleteConfirmationDialogAction(it)) },
    )
    DeleteSubcategoriesDialog(
            category = state.deleteWithSubCategoriesState,
            onDeleteSubCategories = { listener(DeleteThisAndSubCategories) },
            onDeleteJustThis = { listener(DeleteConfirmationDialogAction(DeleteConfirmationDialogIntent.Ok)) },
            onCancel = { listener(DeleteConfirmationDialogAction(DeleteConfirmationDialogIntent.Cancel)) }
    )

    ManageScaffold(
            displayItems = state.categories ?: listOf(),
            addFabContentDescription = "Add category",
            onAddFabClicked = { listener(AddCategoryClicked) },
            currentTab = ManageTabSwitcherItem.CATEGORIES,
            onTabSwitcherClicked = { listener(TabClicked(it)) },
            onItemClicked = { if (it != null) listener(CategoryClicked(it)) },
            itemContent = { CategoryItem(category = it) },
    )
}

@Composable
fun DeleteSubcategoriesDialog(
        category: Category?,
        onDeleteSubCategories: () -> Unit,
        onDeleteJustThis: () -> Unit,
        onCancel: () -> Unit,
) {
    NummiDialog(
            isShown = category != null,
            title = "Delete",
            onCancelListener = onCancel,
    ) {
        Text(
                text = "Are you sure you want to delete " + category?.getItemName() + "? This item has sub categories.",
                modifier = Modifier.padding(bottom = 10.dp)
        )
        BorderedItem(
                onClick = onDeleteSubCategories,
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
        ) {
            Text(
                    text = "Yes, delete it and all sub categories",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(NummiTheme.dimens.listItemPadding)
            )
        }
        BorderedItem(
                onClick = onDeleteJustThis,
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
        ) {
            Text(
                    text = "Yes, just delete this category",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(NummiTheme.dimens.listItemPadding)
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

@Preview(
        heightDp = 700,
        widthDp = 400,
)
@Composable
fun Dialog_ManageCategoriesScreen_Preview() {
    NummiScreenPreviewWrapper {
        DeleteSubcategoriesDialog(
                category = CategoryProvider.basic[0],
                onDeleteSubCategories = {},
                onDeleteJustThis = {},
                onCancel = {},
        )
    }
}
