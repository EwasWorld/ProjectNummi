package com.eywa.projectnummi.database.transaction

import com.eywa.projectnummi.utils.DateUtils
import java.util.*

data class TransactionsFilters(
        val from: Calendar = DateUtils.currentDate().apply { add(Calendar.MONTH, -1) },
        val to: Calendar = DateUtils.currentDate(),
        val selectedAccountIds: List<Int?> = emptyList(),
        val selectedCategoryIds: List<Int?> = emptyList(),
        val selectedPersonIds: List<Int?> = emptyList(),
        val showIncoming: Boolean = true,
        val showOutgoing: Boolean = true,
)
