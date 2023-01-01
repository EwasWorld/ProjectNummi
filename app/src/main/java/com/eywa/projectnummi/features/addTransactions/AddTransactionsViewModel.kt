package com.eywa.projectnummi.features.addTransactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.TempInMemoryDb
import com.eywa.projectnummi.features.addTransactions.AddTransactionsIntent.*
import com.eywa.projectnummi.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class AddTransactionsViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddTransactionsState())
    val state = _state.asStateFlow()

    fun handle(action: AddTransactionsIntent) {
        when (action) {
            is DateIncremented -> {
                _state.update {
                    val dateCopy = it.date.clone() as Calendar
                    it.copy(date = (dateCopy.apply { add(Calendar.DATE, action.daysAdded) }))
                }
            }
            is DateChanged -> {
                _state.update { it.copy(date = action.date) }
            }
            is AmountChanged -> {
                _state.update { it.copy(amount = action.amount) }
            }
            is NameChanged -> {
                _state.update { it.copy(name = action.name) }
            }
            CreateTransaction -> viewModelScope.launch {
                val oldState = state.value
                _state.update { it.copy(amount = "", name = "") }
                TempInMemoryDb.addTransaction(
                        Transaction(
                                id = 0,
                                date = oldState.date,
                                name = oldState.name,
                                amount = (oldState.amount.toDouble() * 100).roundToInt(),
                        )
                )
            }
        }
    }
}
