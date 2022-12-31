package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.model.Transaction

data class ViewTransactionsState(
        val transactions: List<Transaction> = listOf(),
)
