package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.model.Transaction

object TransactionProvider {
    val basic = listOf(1_50, 30, 1000_00, 13_59)
            .mapIndexed { index, amount -> Transaction(index, amount) }
}