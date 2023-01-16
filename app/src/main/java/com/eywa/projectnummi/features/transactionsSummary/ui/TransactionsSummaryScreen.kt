package com.eywa.projectnummi.features.transactionsSummary.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.eywa.projectnummi.features.transactionsSummary.TransactionSummaryViewModel
import com.eywa.projectnummi.features.transactionsSummary.TransactionsSummaryIntent
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryState
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryTabSwitcherItem
import com.eywa.projectnummi.sharedUi.TabSwitcher
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun TransactionsSummaryScreen(
        viewModel: TransactionSummaryViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

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
        ) {
            Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(NummiTheme.dimens.screenPadding)

            ) {
                when (it) {
                    TransactionsSummaryTabSwitcherItem.FILTERS -> TransactionsSummaryFiltersScreen(state, listener)
                    TransactionsSummaryTabSwitcherItem.PIE_CHART -> TransactionsSummaryPieScreen(state, listener)
                }
            }
        }
    }
}
