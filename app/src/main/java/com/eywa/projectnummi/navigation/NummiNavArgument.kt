package com.eywa.projectnummi.navigation

fun String.toNummiNavArgument() =
        try {
            NummiNavArgument.valueOf(
                    Regex("[A-Z]").replace(this) { "_" + it.value }.uppercase()
            )
        }
        catch (e: IllegalArgumentException) {
            null
        }

enum class NummiNavArgument {
    TRANSACTION_ID,
    RECURRING_TRANSACTIONS,
    INIT_FROM_RECURRING_TRANSACTION,
    ;

    fun toArgName() = Regex("_[a-z]").replace(name.lowercase()) { it.value.drop(1).uppercase() }
}
