package com.eywa.projectnummi.features.addTransactions

sealed class AddTransactionsIntent {
    object CreateTransaction : AddTransactionsIntent()
    data class AmountChanged(val amount: String) : AddTransactionsIntent()
}
