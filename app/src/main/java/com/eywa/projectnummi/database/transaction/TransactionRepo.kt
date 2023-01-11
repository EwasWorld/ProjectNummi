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

    @Transaction
    private suspend fun swapOrder(transaction0: DatabaseTransaction, transaction1: DatabaseTransaction) {
        transactionDao.update(transaction0.copy(order = -1))
        transactionDao.update(transaction1.copy(order = transaction0.order))
        transactionDao.update(transaction0.copy(order = transaction1.order))
    }

    @Transaction
    suspend fun decreaseOrder(transaction: DatabaseTransaction) {
        val swapWith = transactionDao.getOrderAbove(transaction.date, transaction.order).first()
                ?: return
        swapOrder(transaction, swapWith)
    }

    @Transaction
    suspend fun increaseOrder(transaction: DatabaseTransaction) {
        val swapWith = transactionDao.getOrderBelow(transaction.date, transaction.order).first()
                ?: return
        swapOrder(transaction, swapWith)
    }
}
