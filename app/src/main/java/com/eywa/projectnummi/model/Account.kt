package com.eywa.projectnummi.model

import com.eywa.projectnummi.database.account.DatabaseAccount

data class Account(
        val id: Int,
        val name: String,
        /**
         * Credit, debit, cash, etc.
         */
        val type: String?,
) {
    constructor(dbAccount: DatabaseAccount) : this(dbAccount.id, dbAccount.name, dbAccount.type)

    fun asDbAccount() = DatabaseAccount(id, name, type)
}
