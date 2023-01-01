package com.eywa.projectnummi.model

import java.util.*

data class Transaction(
        val id: Int,
        val date: Calendar,
        val name: String,
        val amount: Int,
)
