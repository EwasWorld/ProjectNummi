package com.eywa.projectnummi.database

import com.eywa.projectnummi.model.Category
import com.eywa.projectnummi.model.Transaction
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.model.providers.TransactionProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

object TempInMemoryDb {
    val transactions = MutableStateFlow(TransactionProvider.basic)
    val categories = MutableStateFlow(CategoryProvider.basic)

    suspend fun addTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
        transactions.update { it.plus(transaction) }
    }

    suspend fun addCategory(category: Category) = withContext(Dispatchers.IO) {
        categories.update { it.plus(category) }
        category.id
    }
}
