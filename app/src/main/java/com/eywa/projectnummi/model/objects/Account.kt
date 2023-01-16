package com.eywa.projectnummi.model.objects

import com.eywa.projectnummi.database.account.DatabaseAccount
import com.eywa.projectnummi.model.HasNameAndId

data class Account(
        val id: Int,
        val name: String,
        /**
         * Credit, debit, cash, etc.
         */
        val type: String?,
) : HasNameAndId {
    constructor(dbAccount: DatabaseAccount) : this(dbAccount.id, dbAccount.name, dbAccount.type)

    fun asDbAccount() = DatabaseAccount(id, name, type)

    override fun getItemName(): String = name

    override fun getItemId(): Int = id
}
