package com.eywa.projectnummi.database

import com.eywa.projectnummi.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

object TempInMemoryDb {
    val transactions = MutableStateFlow(
            listOf(1_50, 30, 1000_00, 13_59)
                    .mapIndexed { index, amount -> Transaction(index, amount) }
    )

    suspend fun addTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
        transactions.update { it.plus(transaction) }
    }
}
