package com.eywa.projectnummi.features.transactionsSummary.state

import com.eywa.projectnummi.model.HasName

enum class TransactionsSummaryTabSwitcherItem(val text: String) : HasName {
    FILTERS("Filters"),
    PIE_CHART("Graph"),
//  TODO   TRANSACTIONS("Transactions"),
    ;

    override fun getItemName(): String = text
}
