package com.eywa.projectnummi.features.transactionsSummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.database.transaction.TransactionsFilters
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryState
import com.eywa.projectnummi.model.objects.Account
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.model.objects.Person
import com.eywa.projectnummi.model.objects.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionSummaryViewModel @Inject constructor(
        db: NummiDatabase,
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionsSummaryState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            state.map { it.filtersState }.distinctUntilChanged().collectLatest { filters ->
                db.transactionRepo().get(filters).collect { dbTransactions ->
                    val transactions = dbTransactions.map { (transaction, amounts) ->
                        Transaction(transaction, amounts)
                    }
                    _state.update { it.copy(transactions = transactions) }
                }
            }
        }

        viewModelScope.launch {
            db.categoryRepo().get().collect { categories ->
                _state.update { it.copy(categories = categories.map { dbCat -> Category(dbCat) }) }
            }
        }
        viewModelScope.launch {
            db.personRepo().get().collect { people ->
                _state.update { it.copy(people = people.map { dbPerson -> Person(dbPerson) }) }
            }
        }
        viewModelScope.launch {
            db.accountRepo().get().collect { accounts ->
                _state.update { it.copy(accounts = accounts.map { dbAccount -> Account(dbAccount) }) }
            }
        }
    }

    fun handle(action: TransactionsSummaryIntent) {
        when (action) {
            is TransactionsSummaryIntent.NavigationTabClicked -> _state.update { it.copy(currentScreen = action.tab) }
            is TransactionsSummaryIntent.FilterIntent -> handleFilterIntent(action)
            is TransactionsSummaryIntent.SummaryIntent -> handleSummaryIntent(action)
        }
    }

    private fun handleFilterIntent(action: TransactionsSummaryIntent.FilterIntent) {
        when (action) {
            TransactionsSummaryIntent.AccountClicked -> { /* TODO */
            }
            TransactionsSummaryIntent.CategoryClicked -> { /* TODO */
            }
            TransactionsSummaryIntent.PersonClicked -> { /* TODO */
            }
            TransactionsSummaryIntent.GroupingClicked -> { /* TODO */
            }
            is TransactionsSummaryIntent.FromDateChanged ->
                updateFilterState { it.copy(from = action.date) }
            is TransactionsSummaryIntent.ToDateChanged ->
                updateFilterState { it.copy(to = action.date) }
            TransactionsSummaryIntent.ToggleShowIncoming ->
                updateFilterState { it.copy(showIncoming = !it.showIncoming) }
            TransactionsSummaryIntent.ToggleShowOutgoing ->
                updateFilterState { it.copy(showOutgoing = !it.showOutgoing) }
            TransactionsSummaryIntent.ToggleOutgoingIsPositive ->
                _state.update { it.copy(outgoingIsPositive = !it.outgoingIsPositive) }
        }
    }

    private fun updateFilterState(update: (TransactionsFilters) -> TransactionsFilters) =
            _state.update { it.copy(filtersState = update(it.filtersState)) }

    private fun handleSummaryIntent(action: TransactionsSummaryIntent.SummaryIntent) {
        when (action) {
            is TransactionsSummaryIntent.SummaryItemSelected ->
                _state.update { it.copy(selectedItemIndex = action.index) }
            is TransactionsSummaryIntent.SummaryPieClicked -> {
                val degrees = Math.toDegrees(action.polar.theta.toDouble()).toFloat()
                _state.update { it.copy(selectedItemIndex = it.getSegmentIndex(degrees)) }
            }
        }
    }
}
