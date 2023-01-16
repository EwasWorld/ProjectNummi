package com.eywa.projectnummi.features.manageAccounts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eywa.projectnummi.features.manageAccounts.ManageAccountsIntent.*
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.sharedUi.ManageScaffold
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.account.AccountItem
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialog
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialog
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialog
import com.eywa.projectnummi.sharedUi.navigateToManageTab
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem
import kotlinx.coroutines.launch

@Composable
fun ManageAccountsScreen(
        navController: NavController,
        viewModel: ManageAccountsViewModel = hiltViewModel(),
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

    ManageAccountsScreen(state = state.value, listener = { viewModel.handle(it) })
}

@Composable
fun ManageAccountsScreen(
        state: ManageAccountsState,
        listener: (ManageAccountsIntent) -> Unit,
) {
    CreateAccountDialog(
            isShown = true,
            state = state.createDialogState,
            listener = { listener(CreateAccountDialogAction(it)) },
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
            displayItems = state.accounts?.sortedBy { it.name } ?: listOf(),
            addFabContentDescription = "Add account",
            onAddFabClicked = { listener(AddAccountClicked) },
            currentTab = ManageTabSwitcherItem.ACCOUNTS,
            onTabSwitcherClicked = { listener(TabClicked(it)) },
            onItemClicked = { if (it != null) listener(AccountClicked(it)) },
            itemContent = { AccountItem(account = it) }
    )
}

@Preview
@Composable
fun ManageAccountsScreen_Preview() {
    NummiScreenPreviewWrapper {
        ManageAccountsScreen(
                state = ManageAccountsState(
                        accounts = AccountProvider.basic,
                )
        ) {}
    }
}
