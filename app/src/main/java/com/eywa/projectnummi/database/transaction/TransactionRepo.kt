package com.eywa.projectnummi.database.transaction

import androidx.room.Transaction
import com.eywa.projectnummi.database.amount.AmountDao
import com.eywa.projectnummi.database.amount.DatabaseAmount
import kotlinx.coroutines.flow.first

class TransactionRepo(
        private val transactionDao: TransactionDao,
        private val amountDao: AmountDao,
) {
    fun getFull(isRecurring: Boolean = false) = transactionDao.getFull(isRecurring)
    fun getFull(id: Int) = transactionDao.getFull(id)
    fun get(filters: TransactionsFilters) =
            transactionDao.get(
                    from = filters.from,
                    to = filters.to,
                    accountIds = filters.selectedAccountIds.filterNotNull(),
                    allowAccountIsNull = filters.selectedAccountIds.contains(null),
                    overrideShowAllAccounts = filters.selectedAccountIds.isEmpty(),
                    categoryIds = filters.selectedCategoryIds.filterNotNull(),
                    allowCategoryIsNull = filters.selectedCategoryIds.contains(null),
                    overrideShowAllCategories = filters.selectedCategoryIds.isEmpty(),
                    personIds = filters.selectedPersonIds.filterNotNull(),
                    allowPersonIsNull = filters.selectedPersonIds.contains(null),
                    overrideShowAllPeople = filters.selectedPersonIds.isEmpty(),
                    isOutgoing = filters.showOutgoing,
                    overrideShowInAndOut = filters.showOutgoing == filters.showIncoming,
            )

    suspend fun delete(transaction: DatabaseTransaction) = transactionDao.delete(transaction)

    /**
     * Sets [DatabaseTransaction.order] if <= 0 and [DatabaseAmount.transactionId] before inserting
     */
    @Transaction
    suspend fun insert(transaction: DatabaseTransaction, amounts: List<DatabaseAmount>) {
        val maxOrder = transactionDao.getMaxOrder(transaction.date).first() ?: 0
        val newOrder = transaction.order.takeIf { it > 0 } ?: (maxOrder + 1)
        val id = transactionDao.insert(transaction.copy(order = newOrder)).toInt()
        amountDao.insert(*amounts.map { it.copy(transactionId = id) }.toTypedArray())
    }

    /**
     * - Updates [updatedTransaction] (sets [DatabaseTransaction.order] if [DatabaseTransaction.date] has changed)
     * - Inserts all items in [updatedAmounts] with an id of 0, updates all others
     * - Deletes all [originalAmounts] whose ids don't appear in [updatedAmounts]
     */
    @Transaction
    suspend fun update(
            updatedTransaction: DatabaseTransaction,
            updatedAmounts: List<DatabaseAmount>,
            originalTransaction: DatabaseTransaction,
            originalAmounts: List<DatabaseAmount>,
    ) {
        require(updatedTransaction.id == originalTransaction.id) { "Transaction ids don't match" }
        require(updatedAmounts.all { it.transactionId == originalTransaction.id }) { "updatedAmounts transactionIds don't match" }
        require(originalAmounts.all { it.transactionId == originalTransaction.id }) { "originalAmounts transactionIds don't match" }
        require(originalAmounts.none { it.id == 0 }) { "originalAmounts has 0 id item" }

        /*
         * Transaction (update order if necessary)
         */
        var finalTransaction = updatedTransaction
        if (updatedTransaction.date != originalTransaction.date) {
            val maxOrder = transactionDao.getMaxOrder(updatedTransaction.date).first() ?: 0
            finalTransaction = finalTransaction.copy(order = maxOrder + 1)
        }
        transactionDao.update(finalTransaction)

        /*
         * Amount (delete unnecessary and update the rest)
         */
        val usedIds = updatedAmounts.map { it.id }
        val toDelete = originalAmounts.filter { !usedIds.contains(it.id) }
        val (toInsert, toUpdate) = updatedAmounts.partition { it.id == 0 }
        amountDao.delete(*toDelete.toTypedArray())
        amountDao.insert(*toInsert.toTypedArray())
        amountDao.update(*toUpdate.toTypedArray())
    }

    /**
     * Swaps the [DatabaseTransaction.order]s of [transaction0] and [transaction1]
     */
    @Transaction
    private suspend fun swapOrder(transaction0: DatabaseTransaction, transaction1: DatabaseTransaction) {
        transactionDao.update(transaction0.copy(order = -1))
        transactionDao.update(transaction1.copy(order = transaction0.order))
        transactionDao.update(transaction0.copy(order = transaction1.order))
    }

    /**
     * Swaps the [DatabaseTransaction.order] of [transaction] with that of the [DatabaseTransaction] that has
     * the next lowest [DatabaseTransaction.order] and the same [DatabaseTransaction.date].
     * Does nothing if there is no [DatabaseTransaction] matches this criteria
     */
    @Transaction
    suspend fun decreaseOrder(transaction: DatabaseTransaction) {
        val swapWith = transactionDao.getOrderAbove(transaction.date, transaction.order).first()
                ?: return
        swapOrder(transaction, swapWith)
    }

    /**
     * Swaps the [DatabaseTransaction.order] of [transaction] with that of the [DatabaseTransaction] that has
     * the next highest [DatabaseTransaction.order] and the same [DatabaseTransaction.date].
     * Does nothing if there is no [DatabaseTransaction] matches this criteria
     */
    @Transaction
    suspend fun increaseOrder(transaction: DatabaseTransaction) {
        val swapWith = transactionDao.getOrderBelow(transaction.date, transaction.order).first()
                ?: return
        swapOrder(transaction, swapWith)
    }
}
