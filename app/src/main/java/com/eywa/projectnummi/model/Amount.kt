package com.eywa.projectnummi.model

data class Amount(
        val category: Category?,
        /**
         * Null for default person
         */
        val person: Person?,
        /**
         * In pennies
         */
        val amount: Int,
)
