package com.eywa.projectnummi.database

import com.eywa.projectnummi.model.Transaction
import com.eywa.projectnummi.model.providers.TransactionProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

object TempInMemoryDb {
    val transactions = MutableStateFlow(TransactionProvider.basic)

    suspend fun addTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
        transactions.update { it.plus(transaction) }
    }
}
