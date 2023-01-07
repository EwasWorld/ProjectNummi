package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.model.Account

object AccountProvider {
    val basic = listOf(
            "Cash" to "Cash",
            "Nationwide" to "Debit",
            "AMEX" to "Credit",
    ).mapIndexed { index, (name, type) -> Account(index, name, type) }
}
