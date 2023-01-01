package com.eywa.projectnummi.features.addTransactions

import com.eywa.projectnummi.ui.utils.DateUtils
import java.util.*

data class AddTransactionsState(
        val date: Calendar = DateUtils.currentDate(),
        val amount: String = "",
        val name: String = "",
        val isOutgoing: Boolean = true,
)
