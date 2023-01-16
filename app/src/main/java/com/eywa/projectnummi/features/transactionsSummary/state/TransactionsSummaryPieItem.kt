package com.eywa.projectnummi.features.transactionsSummary.state

import androidx.compose.ui.graphics.Color

data class TransactionsSummaryPieItem(
        val name: String?,
        val color: Color?,
        val amount: Int,
        val startAngleDegrees: Float? = null,
        val arcAngleDegrees: Float? = null,
)
