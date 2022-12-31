package com.eywa.projectnummi.features.viewTransactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.TempInMemoryDb
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ViewTransactionsViewModel : ViewModel() {
    private val _state = MutableStateFlow(ViewTransactionsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            TempInMemoryDb.transactions.collect { transactions ->
                _state.update { it.copy(transactions = transactions) }
            }
        }
    }
}
