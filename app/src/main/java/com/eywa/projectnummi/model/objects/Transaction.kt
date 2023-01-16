package com.eywa.projectnummi.model.objects

import com.eywa.projectnummi.database.amount.FullDatabaseAmount
import com.eywa.projectnummi.database.transaction.DatabaseTransaction
import com.eywa.projectnummi.database.transaction.DatabaseTransactionWithFullAccount
import com.eywa.projectnummi.database.transaction.FullDatabaseTransaction
import com.eywa.projectnummi.features.viewTransactions.descendingDateTransactionComparator
import com.eywa.projectnummi.model.HasNameAndId
import java.util.*

// TODO Transaction.note feature
data class Transaction(
        val id: Int,
        val date: Calendar,
        val name: String,
        val amounts: List<Amount>,
        /**
         * True if money is leaving the account, false if it's entering the account
         */
        val isOutgoing: Boolean = true,
        val account: Account? = null,
        /**
         * @see [descendingDateTransactionComparator]
         */
        val order: Int = -1,
) : HasNameAndId {
    constructor(
            dbTransaction: FullDatabaseTransaction,
    ) : this(
            id = dbTransaction.transaction.id,
            date = dbTransaction.transaction.date,
            name = dbTransaction.transaction.name,
            amounts = dbTransaction.amounts.map { Amount(it) },
            isOutgoing = dbTransaction.transaction.isOutgoing,
            account = dbTransaction.account?.let { Account(it) },
            order = dbTransaction.transaction.order,
    )

    constructor(
            dbTransaction: DatabaseTransactionWithFullAccount,
            dbAmounts: List<FullDatabaseAmount>,
    ) : this(
            id = dbTransaction.transaction.id,
            date = dbTransaction.transaction.date,
            name = dbTransaction.transaction.name,
            amounts = dbAmounts.map { Amount(it) },
            isOutgoing = dbTransaction.transaction.isOutgoing,
            account = dbTransaction.account?.let { Account(it) },
            order = dbTransaction.transaction.order,
    )

    fun asDbTransaction() = DatabaseTransaction(
            id = id,
            date = date,
            name = name,
            accountId = account?.id,
            isOutgoing = isOutgoing,
            order = order,
    )

    fun getDbAmounts(id: Int? = null) = amounts.map { it.asDatabaseAmount(id) }

    override fun getItemName(): String = name

    override fun getItemId(): Int = id
}
