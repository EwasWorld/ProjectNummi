package com.eywa.projectnummi.model

// TODO Account feature
data class Account(
        val id: Int,
        val name: String,
        /**
         * Credit, debit, cash, etc.
         */
        val type: String,
)
