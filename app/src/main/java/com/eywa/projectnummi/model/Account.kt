package com.eywa.projectnummi.model

import com.eywa.projectnummi.database.account.DatabaseAccount

data class Account(
        val id: Int,
        val name: String,
        /**
         * Credit, debit, cash, etc.
         */
        val type: String?,
) : NamedItem {
    constructor(dbAccount: DatabaseAccount) : this(dbAccount.id, dbAccount.name, dbAccount.type)

    fun asDbAccount() = DatabaseAccount(id, name, type)

    override fun getItemName(): String = name
}
