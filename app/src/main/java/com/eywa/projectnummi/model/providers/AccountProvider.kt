package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.model.objects.Account

object AccountProvider {
    val basic = listOf(
            "Cash" to "Cash",
            "NatWest" to "Debit card",
            "AMEX" to "Credit card",
    ).mapIndexed { index, (name, type) -> Account(index + 1, name, type) }
}
