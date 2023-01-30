package com.eywa.projectnummi.features.transactionsSummary.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eywa.projectnummi.features.transactionsSummary.TransactionSummaryViewModel
import com.eywa.projectnummi.features.transactionsSummary.TransactionsSummaryIntent
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryState
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryTabSwitcherItem
import com.eywa.projectnummi.features.viewTransactions.HandleViewTransactionsExtras
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsColumn
import com.eywa.projectnummi.sharedUi.TabSwitcher
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun TransactionsSummaryScreen(
        navController: NavController,
        viewModel: TransactionSummaryViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    HandleViewTransactionsExtras(
            state = state.value.viewTransactionState,
            navController = navController,
            viewModel = viewModel,
    )

    TransactionsSummaryScreen(
            state = state.value,
            listener = { viewModel.handle(it) },
    )
}

@Composable
fun TransactionsSummaryScreen(
        state: TransactionsSummaryState,
        listener: (TransactionsSummaryIntent) -> Unit,
) {
    Column {
        TabSwitcher(
                items = TransactionsSummaryTabSwitcherItem.values().toList(),
                selectedItem = state.currentScreen,
                itemClickedListener = { listener(TransactionsSummaryIntent.NavigationTabClicked(it)) },
        )
        Crossfade(
                targetState = state.currentScreen,
                modifier = Modifier
                        .weight(1f)
        ) { tab ->
            when (tab) {
                TransactionsSummaryTabSwitcherItem.FILTERS ->
                    ScreenWrapper { TransactionsSummaryFiltersScreen(state, listener) }
                TransactionsSummaryTabSwitcherItem.PIE_CHART ->
                    ScreenWrapper { TransactionsSummaryPieScreen(state, listener) }
                TransactionsSummaryTabSwitcherItem.TRANSACTIONS ->
                    ViewTransactionsColumn(
                            state = state.viewTransactionState,
                            listener = { listener(TransactionsSummaryIntent.ViewTransactionsAction(it)) }
                    )
            }
        }
    }
}

@Composable
private fun ScreenWrapper(
        content: @Composable BoxScope.() -> Unit,
) = Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(NummiTheme.dimens.screenPadding),
        content = content,
)
