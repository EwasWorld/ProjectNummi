package com.eywa.projectnummi.features.viewTransactions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsIntent.*
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.model.providers.TransactionProvider
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.TabSwitcher
import com.eywa.projectnummi.sharedUi.TransactionItemFull
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialog
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialog
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem
import com.eywa.projectnummi.theme.NummiTheme
import kotlinx.coroutines.launch


/**
 * Sorts transactions as follows (in order):
 * - Nulls first
 * - Latest [Transaction.date] first
 * - Higher [Transaction.order] first
 */
val descendingDateTransactionComparator: Comparator<Transaction> = object : Comparator<Transaction> {
    override fun compare(t0: Transaction?, t1: Transaction?): Int {
        if (t0 == null && t1 == null) return 0
        if (t1 == null) return 1
        if (t0 == null) return -1

        val descendingDateComparison = t1.date.compareTo(t0.date)
        if (descendingDateComparison != 0) return descendingDateComparison

        return t1.order.compareTo(t0.order)
    }
}

@Composable
fun ViewTransactionsScreen(
        navController: NavController,
        viewModel: ViewTransactionsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    HandleViewTransactionsExtras(
            state = state.value,
            navController = navController,
            viewModel = viewModel,
    )

    Box(
            modifier = Modifier.fillMaxSize()
    ) {
        ViewTransactionsColumn(
                state = state.value,
                headerContent = {
                    if (viewModel.isRecurring) {
                        TabSwitcher(
                                items = ManageTabSwitcherItem.values().toList(),
                                selectedItem = ManageTabSwitcherItem.RECURRING,
                                itemClickedListener = { viewModel.handleViewTransactionIntent(TabClicked(it)) },
                        )
                    }
                },
                listener = { viewModel.handleViewTransactionIntent(it) },
        )
        // TODO show/hide on scroll up/down
        FloatingActionButton(
                backgroundColor = NummiTheme.colors.fab.main,
                contentColor = NummiTheme.colors.fab.content,
                onClick = { viewModel.handleViewTransactionIntent(AddClicked) },
                modifier = Modifier
                        .padding(NummiTheme.dimens.fabToScreenEdgePadding)
                        .align(Alignment.BottomEnd)
        ) {
            Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add transactions",
            )
        }
    }
}

@Composable
fun HandleViewTransactionsExtras(
        state: ViewTransactionsState,
        navController: NavController,
        viewModel: ViewTransactionsViewModelHelper,
) {
    LaunchedEffect(state.extras) {
        launch {
            state.extras.forEach { it.handle(navController, viewModel) }
        }
    }
}

@Composable
fun ViewTransactionsColumn(
        state: ViewTransactionsState,
        modifier: Modifier = Modifier,
        headerContent: (@Composable () -> Unit)? = null,
        listener: (ViewTransactionsIntent) -> Unit,
) {
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

    Column(
            modifier = modifier.fillMaxSize()
    ) {
        headerContent?.invoke()

        LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(NummiTheme.dimens.screenPadding),
                modifier = Modifier.weight(1f)
        ) {
            items(state.transactions) { item ->
                TransactionItemFull(
                        showCompact = item.id != state.selectedTransactionId,
                        item = item,
                        modifier = Modifier.pointerInput(item) {
                            detectTapGestures(
                                    onTap = { listener(TransactionClicked(item)) },
                                    onLongPress = { listener(TransactionLongClicked(item)) },
                            )
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun ViewTransactionsScreen_Preview() {
    NummiScreenPreviewWrapper {
        ViewTransactionsColumn(
                ViewTransactionsState(
                        transactions = TransactionProvider.basic,
                )
        ) {}
    }
}

@Preview
@Composable
fun Recurring_ViewTransactionsScreen_Preview() {
    NummiScreenPreviewWrapper {
        ViewTransactionsColumn(
                ViewTransactionsState(
                        transactions = TransactionProvider.basic.map { it.copy(isRecurring = true) },
                )
        ) {}
    }
}
