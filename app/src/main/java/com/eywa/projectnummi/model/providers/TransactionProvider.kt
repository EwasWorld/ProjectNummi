package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.model.Transaction

object TransactionProvider {
    val basic = listOf(
            1_50 to "Sainsburys",
            30 to "Gym",
            1000_00 to "Salary",
            13_59 to "Amazon",
    ).mapIndexed { index, (amount, name) -> Transaction(index, name, amount) }
}