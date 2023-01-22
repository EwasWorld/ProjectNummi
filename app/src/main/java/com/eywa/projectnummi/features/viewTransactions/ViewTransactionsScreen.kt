package com.eywa.projectnummi.features.viewTransactions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsIntent.*
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.model.providers.TransactionProvider
import com.eywa.projectnummi.navigation.NummiNavArgument
import com.eywa.projectnummi.navigation.NummiNavRoute
import com.eywa.projectnummi.sharedUi.CornerTriangleBox
import com.eywa.projectnummi.sharedUi.CornerTriangleShapeState
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.TabSwitcher
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialog
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialog
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.utils.DateTimeFormat
import com.eywa.projectnummi.utils.asCurrency
import com.eywa.projectnummi.utils.div100String
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

    LaunchedEffect(state.value.extras) {
        launch {
            val extras = state.value.extras
            extras.get(ViewTransactionsExtra.EditTransaction::class)?.let {
                NummiNavRoute.EDIT_TRANSACTIONS.navigate(
                        navController, mapOf(NummiNavArgument.TRANSACTION_ID to it.transaction.id.toString())
                )
                viewModel.handle(ClearExtra(it))
            }
            extras.get(ViewTransactionsExtra.NewTransactionFromRecurring::class)?.let {
                NummiNavRoute.ADD_TRANSACTIONS_FROM_RECURRING.navigate(
                        navController,
                        mapOf(
                                NummiNavArgument.TRANSACTION_ID to it.transaction.id.toString(),
                                NummiNavArgument.INIT_FROM_RECURRING_TRANSACTION to true.toString(),
                        )
                )
                viewModel.handle(ClearExtra(it))
            }
            extras.get(ViewTransactionsExtra.ManageTabChanged::class)?.let {
                it.tab.navRoute.navigate(navController)
                viewModel.handle(ClearExtra(it))
            }
            extras.get(ViewTransactionsExtra.AddTransactions::class)?.let {
                NummiNavRoute.ADD_TRANSACTIONS.navigate(
                        navController,
                        mapOf(NummiNavArgument.TICK_SAVE_AS_RECURRING to state.value.isRecurring.toString()),
                )
                viewModel.handle(ClearExtra(it))
            }
        }
    }

    ViewTransactionsScreen(
            state = state.value,
            listener = { viewModel.handle(it) },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ViewTransactionsScreen(
        state: ViewTransactionsState,
        listener: (ViewTransactionsIntent) -> Unit,
) {
    val displayItems = if (state.isRecurring) state.transactions.sortedBy { it.name }
    else state.transactions.sortedWith(descendingDateTransactionComparator)

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
        Column {
            if (state.isRecurring) {
                TabSwitcher(
                        items = ManageTabSwitcherItem.values().toList(),
                        selectedItem = ManageTabSwitcherItem.RECURRING,
                        itemClickedListener = { listener(TabClicked(it)) },
                )
            }
            LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(NummiTheme.dimens.screenPadding),
                    modifier = Modifier.weight(1f)
            ) {
                items(displayItems) { item ->
                    Surface(
                            color = Color.Transparent,
                            border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
                            shape = NummiTheme.shapes.generalListItem,
                            onClick = { listener(TransactionClicked(item)) },
                            modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                                contentAlignment = Alignment.Center,
                        ) {
                            val colorTriangleSize =
                                    with(LocalDensity.current) { NummiTheme.dimens.viewTransactionTriangleSize.toPx() }

                            CornerTriangleBox(
                                    color = NummiTheme.colors.getTransactionColor(item.isOutgoing),
                                    state = CornerTriangleShapeState(
                                            isTop = false,
                                            isLeft = false,
                                            forceSize = colorTriangleSize,
                                            xScale = 0.8f,
                                            yScale = 0.8f,
                                    ),
                                    modifier = Modifier.alpha(0.3f)
                            )
                            if (item.amounts.any { it.category != null }) {
                                CornerTriangleBox(
                                        colors = item.amounts.map { it.category?.displayColor }.reversed(),
                                        state = CornerTriangleShapeState(
                                                isTop = false,
                                                segmentWeights = item.amounts.map { it.amount }.reversed(),
                                                forceSize = colorTriangleSize,
                                                yScale = 1.5f,
                                                usePercentage = false,
                                        ),
                                )
                            }
                            Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 18.dp, vertical = 12.dp)
                            ) {
                                if (item.account != null) {
                                    Text(
                                            text = item.account.name,
                                            color = NummiTheme.colors.appBackground.content,
                                            fontStyle = FontStyle.Italic,
                                    )
                                }
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                            text = item.name,
                                            color = NummiTheme.colors.appBackground.content,
                                            style = NummiTheme.typography.h5,
                                            modifier = Modifier.weight(1f)
                                    )
                                    if (!state.isRecurring) {
                                        Text(
                                                text = DateTimeFormat.SHORT_DATE.format(item.date),
                                                color = NummiTheme.colors.appBackground.content,
                                                modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }
                                Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(5.dp),
                                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
                                ) {
                                    item.amounts.forEach { amount ->
                                        Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            if (amount.category != null) {
                                                Text(
                                                        text = amount.category.name,
                                                        color = NummiTheme.colors.appBackground.content,
                                                        modifier = Modifier.padding(end = 10.dp)
                                                )
                                            }
                                            if (amount.person?.id != null || item.amounts.size > 1) {
                                                Text(
                                                        text = amount.person?.name ?: "Me",
                                                        color = NummiTheme.colors.appBackground.content,
                                                        fontStyle = FontStyle.Italic,
                                                        modifier = Modifier
                                                                .background(
                                                                        NummiTheme.colors.transactionAmountDetail,
                                                                        RoundedCornerShape(100),
                                                                )
                                                                .padding(horizontal = 10.dp, vertical = 2.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                    text = amount.amount.div100String().asCurrency(),
                                                    color = NummiTheme.colors.appBackground.content,
                                            )
                                        }
                                    }
                                }
                                if (item.amounts.size > 1) {
                                    val oneDpWidth = with(LocalDensity.current) { 3.dp.toPx() }
                                    val totalLinesColor = NummiTheme.colors.transactionTotalLines
                                    Box(
                                            modifier = Modifier
                                                    .align(Alignment.End)
                                                    .padding(end = 6.dp, bottom = 5.dp)
                                    ) {
                                        Text(
                                                text = item.amounts.sumOf { it.amount }.div100String().asCurrency(),
                                                color = NummiTheme.colors.appBackground.content,
                                                modifier = Modifier
                                                        .padding(bottom = 4.dp)
                                                        .padding(horizontal = 8.dp)
                                        )
                                        Canvas(
                                                modifier = Modifier.matchParentSize()
                                        ) {
                                            fun customLine(height: Float) = drawLine(
                                                    totalLinesColor,
                                                    Offset(0f, height),
                                                    Offset(size.width, height),
                                                    strokeWidth = oneDpWidth / 2f
                                            )

                                            customLine(0f)
                                            customLine(size.height)
                                            customLine(size.height - oneDpWidth * 1)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // TODO show/hide on scroll up/down
        FloatingActionButton(
                backgroundColor = NummiTheme.colors.fab.main,
                contentColor = NummiTheme.colors.fab.content,
                onClick = { listener(AddClicked) },
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

@Preview
@Composable
fun ViewTransactionsScreen_Preview() {
    NummiScreenPreviewWrapper {
        ViewTransactionsScreen(
                ViewTransactionsState(
                        transactions = TransactionProvider.basic,
                )
        ) {}
    }
}
