package com.eywa.projectnummi.features.transactionsSummary.state

import com.eywa.projectnummi.model.NamedItem

enum class TransactionsSummaryTabSwitcherItem(val text: String) : NamedItem {
    FILTERS("Filters"),
    PIE_CHART("Graph"),
//  TODO   TRANSACTIONS("Transactions"),
    ;

    override fun getItemName(): String = text
}
