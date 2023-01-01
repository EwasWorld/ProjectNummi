package com.eywa.projectnummi.features.addTransactions

import java.util.*

sealed class AddTransactionsIntent {
    object CreateTransaction : AddTransactionsIntent()
    data class DateChanged(val date: Calendar) : AddTransactionsIntent()
    data class DateIncremented(val daysAdded: Int) : AddTransactionsIntent()
    data class AmountChanged(val amount: String) : AddTransactionsIntent()
    data class NameChanged(val name: String) : AddTransactionsIntent()
}
