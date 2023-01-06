package com.eywa.projectnummi.features.viewTransactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewTransactionsViewModel @Inject constructor(
        private val db: NummiDatabase,
) : ViewModel() {
    private val _state = MutableStateFlow(ViewTransactionsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            db.transactionRepo().getFull().collect { transactions ->
                _state.update { it.copy(transactions = transactions.map { dbTrans -> Transaction(dbTrans) }) }
            }
        }
    }
}
