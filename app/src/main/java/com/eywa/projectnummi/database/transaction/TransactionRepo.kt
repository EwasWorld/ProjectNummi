package com.eywa.projectnummi.database.transaction

import androidx.room.Transaction
import com.eywa.projectnummi.database.amount.AmountDao
import com.eywa.projectnummi.database.amount.DatabaseAmount
import kotlinx.coroutines.flow.first

class TransactionRepo(
        private val transactionDao: TransactionDao,
        private val amountDao: AmountDao,
) {
    fun getFull() = transactionDao.getFull()
    suspend fun delete(transaction: DatabaseTransaction) = transactionDao.delete(transaction)
    suspend fun update(vararg transactions: DatabaseTransaction) = transactionDao.update(*transactions)

    /**
     * Sets [DatabaseTransaction.order] and [DatabaseAmount.transactionId] before inserting
     */
    @Transaction
    suspend fun insert(transaction: DatabaseTransaction, amounts: List<DatabaseAmount>) {
        val maxOrder = transactionDao.getMaxOrder(transaction.date).first() ?: 0
        val id = transactionDao.insert(transaction.copy(order = maxOrder + 1)).toInt()
        amountDao.insert(*amounts.map { it.copy(transactionId = id) }.toTypedArray())
    }
}
