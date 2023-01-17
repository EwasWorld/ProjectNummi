package com.eywa.projectnummi.features.transactionsSummary.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.theme.NummiTheme

data class TransactionsSummaryPieItem(
        val name: String?,
        val color: Color?,
        val amount: Int,
        val startAngleDegrees: Float? = null,
        val arcAngleDegrees: Float? = null,
) {
    @Composable
    fun getDisplayColor() = when (name) {
        TransactionsSummaryGrouping.INCOMING_NAME -> NummiTheme.colors.incomingTransaction
        TransactionsSummaryGrouping.OUTGOING_NAME -> NummiTheme.colors.outgoingTransaction
        else -> color ?: NummiTheme.colors.pieChartDefault
    }
}
