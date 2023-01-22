package com.eywa.projectnummi.model.objects

import com.eywa.projectnummi.database.amount.DatabaseAmount
import com.eywa.projectnummi.database.amount.FullDatabaseAmountWithFullCategory

data class Amount(
        val id: Int,
        val category: Category?,
        /**
         * Null for default person
         */
        val person: Person?,
        /**
         * In pennies
         */
        val amount: Int,
) {
    constructor(
            dbAmount: FullDatabaseAmountWithFullCategory,
    ) : this(
            id = dbAmount.amount.id,
            category = dbAmount.category?.let { Category(it) },
            person = dbAmount.person?.let { Person(it) },
            amount = dbAmount.amount.amount
    )

    fun asDatabaseAmount(transactionId: Int?) = DatabaseAmount(
            id = id,
            transactionId = transactionId ?: 0,
            categoryId = category?.id,
            personId = person?.id,
            amount = amount
    )
}
