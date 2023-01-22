package com.eywa.projectnummi.model.objects

import com.eywa.projectnummi.database.amount.FullDatabaseAmountWithFullCategory
import com.eywa.projectnummi.database.transaction.DatabaseTransaction
import com.eywa.projectnummi.database.transaction.DatabaseTransactionWithFullAccount
import com.eywa.projectnummi.database.transaction.FullDatabaseTransactionWithFullCategory
import com.eywa.projectnummi.model.HasNameAndId
import java.util.*

/**
 * @see [DatabaseTransaction]
 */
// TODO Transaction.note feature
data class Transaction(
        val id: Int,
        val date: Calendar,
        val name: String,
        val amounts: List<Amount>,
        val isOutgoing: Boolean = true,
        val account: Account? = null,
        val order: Int = -1,
        val isRecurring: Boolean = false,
) : HasNameAndId {
    constructor(
            dbTransaction: FullDatabaseTransactionWithFullCategory,
    ) : this(
            id = dbTransaction.transaction.id,
            date = dbTransaction.transaction.date,
            name = dbTransaction.transaction.name,
            amounts = dbTransaction.amounts.map { Amount(it) },
            isOutgoing = dbTransaction.transaction.isOutgoing,
            account = dbTransaction.account?.let { Account(it) },
            order = dbTransaction.transaction.order,
            isRecurring = dbTransaction.transaction.isRecurring,
    )

    constructor(
            dbTransaction: DatabaseTransactionWithFullAccount,
            dbAmounts: List<FullDatabaseAmountWithFullCategory>,
    ) : this(
            id = dbTransaction.transaction.id,
            date = dbTransaction.transaction.date,
            name = dbTransaction.transaction.name,
            amounts = dbAmounts.map { Amount(it) },
            isOutgoing = dbTransaction.transaction.isOutgoing,
            account = dbTransaction.account?.let { Account(it) },
            order = dbTransaction.transaction.order,
            isRecurring = dbTransaction.transaction.isRecurring,
    )

    fun asDbTransaction() = DatabaseTransaction(
            id = id,
            date = date,
            name = name,
            accountId = account?.id,
            isOutgoing = isOutgoing,
            order = order,
            isRecurring = isRecurring,
    )

    fun getDbAmounts(id: Int? = null) = amounts.map { it.asDatabaseAmount(id) }

    override fun getItemName(): String = name

    override fun getItemId(): Int = id
}
