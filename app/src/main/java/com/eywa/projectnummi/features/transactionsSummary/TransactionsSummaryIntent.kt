package com.eywa.projectnummi.features.transactionsSummary

import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryTabSwitcherItem
import com.eywa.projectnummi.utils.Polar
import java.util.*

sealed class TransactionsSummaryIntent {
    data class NavigationTabClicked(val tab: TransactionsSummaryTabSwitcherItem) : TransactionsSummaryIntent()

    data class ToDateChanged(val date: Calendar) : FilterIntent()
    data class FromDateChanged(val date: Calendar) : FilterIntent()
    object GroupingClicked : FilterIntent()
    object AccountClicked : FilterIntent()
    object CategoryClicked : FilterIntent()
    object PersonClicked : FilterIntent()
    object ToggleShowIncoming : FilterIntent()
    object ToggleShowOutgoing : FilterIntent()
    object ToggleOutgoingIsPositive : FilterIntent()

    data class SummaryItemSelected(val index: Int) : SummaryIntent()
    data class SummaryPieClicked(val polar: Polar) : SummaryIntent()

    sealed class FilterIntent : TransactionsSummaryIntent()
    sealed class SummaryIntent : TransactionsSummaryIntent()
}
