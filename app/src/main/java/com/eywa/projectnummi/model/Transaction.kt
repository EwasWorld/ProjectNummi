package com.eywa.projectnummi.model

import com.eywa.projectnummi.database.transaction.DatabaseTransaction
import com.eywa.projectnummi.database.transaction.FullDatabaseTransaction
import java.util.*

// TODO Transaction.note feature
data class Transaction(
        val id: Int,
        val date: Calendar,
        val name: String,
        val amount: List<Amount>,
        /**
         * True if money is leaving the account, false if it's entering the account
         */
        val isOutgoing: Boolean = true,
) {
    constructor(
            dbTransaction: FullDatabaseTransaction,
    ) : this(
            id = dbTransaction.transaction.id,
            date = dbTransaction.transaction.date,
            name = dbTransaction.transaction.name,
            amount = dbTransaction.amounts.map { Amount(it) },
            isOutgoing = dbTransaction.transaction.isOutgoing,
    )

    fun asDbTransaction() = DatabaseTransaction(
            id = id,
            date = date,
            name = name,
            accountId = null,
            isOutgoing = isOutgoing,
    )
}
