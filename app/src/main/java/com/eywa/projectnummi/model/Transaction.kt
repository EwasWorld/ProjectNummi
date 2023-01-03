package com.eywa.projectnummi.model

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
)
