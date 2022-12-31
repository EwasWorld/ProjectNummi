package com.eywa.projectnummi.features.addTransactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.TempInMemoryDb
import com.eywa.projectnummi.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class AddTransactionsViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddTransactionsState())
    val state = _state.asStateFlow()

    fun handle(action: AddTransactionsIntent) {
        when (action) {
            is AddTransactionsIntent.AmountChanged -> {
                _state.update { it.copy(enteredAmount = action.amount) }
            }
            AddTransactionsIntent.CreateTransaction -> viewModelScope.launch {
                val amount = state.value.enteredAmount
                _state.update { it.copy(enteredAmount = "") }
                TempInMemoryDb.addTransaction(
                        Transaction(0, (amount.toDouble() * 100).roundToInt())
                )
            }
        }
    }
}
