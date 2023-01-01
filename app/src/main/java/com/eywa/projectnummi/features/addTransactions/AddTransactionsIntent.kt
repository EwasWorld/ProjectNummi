package com.eywa.projectnummi.features.addTransactions

sealed class AddTransactionsIntent {
    object CreateTransaction : AddTransactionsIntent()
    data class AmountChanged(val amount: String) : AddTransactionsIntent()
    data class NameChanged(val name: String) : AddTransactionsIntent()
}
