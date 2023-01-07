package com.eywa.projectnummi.features.manageAccounts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.eywa.projectnummi.components.account.AccountItem
import com.eywa.projectnummi.components.account.createAccountDialog.CreateAccountDialog
import com.eywa.projectnummi.features.manageAccounts.ManageAccountsIntent.AddAccountClicked
import com.eywa.projectnummi.features.manageAccounts.ManageAccountsIntent.CreateAccountDialogAction
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme

@Composable
fun ManageAccountsScreen(
        viewModel: ManageAccountsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    ManageAccountsScreen(state = state.value, listener = { viewModel.handle(it) })
}

@Composable
fun ManageAccountsScreen(
        state: ManageAccountsState,
        listener: (ManageAccountsIntent) -> Unit,
) {
    val displayItems = state.accounts?.sortedBy { it.name } ?: listOf()

    CreateAccountDialog(
            isShown = true,
            state = state.createDialogState,
            listener = { listener(CreateAccountDialogAction(it)) },
    )

    Box {
        LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(NummiTheme.dimens.listItemSpacedBy),
                contentPadding = PaddingValues(NummiTheme.dimens.screenPadding),
                modifier = Modifier.fillMaxSize()
        ) {
            items(displayItems) { item ->
                AccountItem(
                        account = item,
                        onClick = {},
                )
            }
        }

        FloatingActionButton(
                backgroundColor = NummiTheme.colors.fab.main,
                contentColor = NummiTheme.colors.fab.content,
                onClick = { listener(AddAccountClicked) },
                modifier = Modifier
                        .padding(NummiTheme.dimens.fabToScreenEdgePadding)
                        .align(Alignment.BottomEnd)
        ) {
            Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add account",
            )
        }
    }
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
