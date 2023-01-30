package com.eywa.projectnummi.features.transactionsSummary

import com.eywa.projectnummi.features.transactionsSummary.state.TransactionSummarySelectionDialog
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryTabSwitcherItem
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsIntent
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectItemDialogIntent
import com.eywa.projectnummi.utils.Polar
import java.util.*

sealed class TransactionsSummaryIntent {
    data class NavigationTabClicked(val tab: TransactionsSummaryTabSwitcherItem) : TransactionsSummaryIntent()

    data class ToDateChanged(val date: Calendar) : FilterIntent()
    data class FromDateChanged(val date: Calendar) : FilterIntent()
    object ToggleShowIncoming : FilterIntent()
    object ToggleShowOutgoing : FilterIntent()
    object ToggleOutgoingIsPositive : FilterIntent()

    data class OpenSelectDialog(val type: TransactionSummarySelectionDialog) : SelectDialogIntent()
    data class SelectDialogAction(val action: SelectItemDialogIntent) : SelectDialogIntent()

    data class SummaryItemSelected(val index: Int) : SummaryIntent()
    data class SummaryPieClicked(val polar: Polar) : SummaryIntent()

    data class ViewTransactionsAction(val action: ViewTransactionsIntent) : SummaryIntent()

    sealed class FilterIntent : TransactionsSummaryIntent()
    sealed class SummaryIntent : TransactionsSummaryIntent()
    sealed class SelectDialogIntent : TransactionsSummaryIntent()
}
