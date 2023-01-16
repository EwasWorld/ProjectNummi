package com.eywa.projectnummi.features.managePeople

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eywa.projectnummi.features.managePeople.ManagePeopleIntent.*
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.sharedUi.ManageScaffold
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialog
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialog
import com.eywa.projectnummi.sharedUi.navigateToManageTab
import com.eywa.projectnummi.sharedUi.person.PersonItem
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialog
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem
import kotlinx.coroutines.launch

@Composable
fun ManagePeopleScreen(
        navController: NavController,
        viewModel: ManagePeopleViewModel = hiltViewModel(),
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

    ManagePeopleScreen(state = state.value, listener = { viewModel.handle(it) })
}

@Composable
fun ManagePeopleScreen(
        state: ManagePeopleState,
        listener: (ManagePeopleIntent) -> Unit,
) {
    CreatePersonDialog(
            isShown = true,
            state = state.createDialogState,
            listener = { listener(CreatePersonDialogAction(it)) },
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
            displayItems = state.people?.sortedBy { it.name } ?: listOf(),
            addFabContentDescription = "Add person",
            onAddFabClicked = { listener(AddPersonClicked) },
            currentTab = ManageTabSwitcherItem.PEOPLE,
            onTabSwitcherClicked = { listener(TabClicked(it)) },
            onItemClicked = { if (it != null) listener(PersonClicked(it)) },
            itemContent = { PersonItem(person = it) }
    )
}

@Preview
@Composable
fun ManageCategoriesScreen_Preview() {
    NummiScreenPreviewWrapper {
        ManagePeopleScreen(
                state = ManagePeopleState(
                        people = PeopleProvider.basic,
                )
        ) {}
    }
}
