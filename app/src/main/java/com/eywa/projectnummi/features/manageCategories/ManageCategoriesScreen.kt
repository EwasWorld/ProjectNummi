package com.eywa.projectnummi.features.manageCategories

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.*
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.sharedUi.ManageScaffold
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.category.CategoryItem
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialog
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialog
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialog
import com.eywa.projectnummi.sharedUi.navigateToManageTab
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem
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
            isShown = true,
            state = state.deleteDialogState,
            listener = { listener(DeleteConfirmationDialogAction(it)) },
    )

    ManageScaffold(
            displayItems = state.categories?.sortedBy { it.name } ?: listOf(),
            addFabContentDescription = "Add category",
            onAddFabClicked = { listener(AddCategoryClicked) },
            currentTab = ManageTabSwitcherItem.CATEGORIES,
            onTabSwitcherClicked = { listener(TabClicked(it)) },
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
