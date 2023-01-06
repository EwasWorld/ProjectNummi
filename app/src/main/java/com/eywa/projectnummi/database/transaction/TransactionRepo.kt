package com.eywa.projectnummi.database.transaction

import androidx.room.Transaction
import com.eywa.projectnummi.database.amount.AmountDao
import com.eywa.projectnummi.database.amount.DatabaseAmount

class TransactionRepo(
        private val transactionDao: TransactionDao,
        private val amountDao: AmountDao,
) {
    fun getFull() = transactionDao.getFull()
    suspend fun delete(transaction: DatabaseTransaction) = transactionDao.delete(transaction)
    suspend fun update(vararg transactions: DatabaseTransaction) = transactionDao.update(*transactions)

    @Transaction
    suspend fun insert(transaction: DatabaseTransaction, amounts: List<DatabaseAmount>) {
        val id = transactionDao.insert(transaction).toInt()
        amountDao.insert(*amounts.map { it.copy(transactionId = id) }.toTypedArray())
    }
}
