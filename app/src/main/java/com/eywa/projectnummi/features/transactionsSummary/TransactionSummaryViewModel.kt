package com.eywa.projectnummi.features.transactionsSummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.database.transaction.TransactionRepo
import com.eywa.projectnummi.database.transaction.TransactionsFilters
import com.eywa.projectnummi.features.transactionsSummary.TransactionsSummaryIntent.*
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionSummarySelectionDialog.*
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryGrouping
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryState
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsManageItemOptions
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsState
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsViewModelHelper
import com.eywa.projectnummi.model.objects.Account
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.model.objects.Person
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectItemDialogIntent
import com.eywa.projectnummi.utils.ListUtils.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionSummaryViewModel @Inject constructor(
        db: NummiDatabase,
) : ViewModel(), ViewTransactionsViewModelHelper {
    private val _state = MutableStateFlow(TransactionsSummaryState())
    val state = _state.asStateFlow()

    override val isRecurring: Boolean = false
    override val vtTransactionRepo: TransactionRepo = db.transactionRepo()
    override val vtViewModelScope: CoroutineScope = viewModelScope

    override fun updateState(block: (ViewTransactionsState) -> ViewTransactionsState) = _state.update {
        val newState = block(it.viewTransactionStateRaw)
        if (newState.transactions != it.viewTransactionStateRaw.transactions) {
            throw IllegalStateException("Transactions should not be changed as it is being overridden")
        }
        it.copy(viewTransactionStateRaw = newState)
    }

    override fun currentState(): ViewTransactionsState = _state.value.viewTransactionState

    override fun getContextMenuItems(): List<ViewTransactionsManageItemOptions> = listOf(
            ViewTransactionsManageItemOptions.Edit,
            ViewTransactionsManageItemOptions.Delete,
    )

    init {
        vtViewModelScope.launch {
            state.map { it.filtersState }.distinctUntilChanged().collectLatest { filters ->
                db.transactionRepo().get(filters).collect { dbTransactions ->
                    val transactions = dbTransactions.map { (transaction, amounts) ->
                        Transaction(transaction, amounts)
                    }
                    _state.update { it.copy(transactions = transactions) }
                }
            }
        }

        vtViewModelScope.launch {
            db.categoryRepo().getFull().collect { categories ->
                _state.update { it.copy(categories = categories.map { dbCat -> Category(dbCat) }) }
            }
        }
        vtViewModelScope.launch {
            db.personRepo().get().collect { people ->
                _state.update { it.copy(people = people.map { dbPerson -> Person(dbPerson) }) }
            }
        }
        vtViewModelScope.launch {
            db.accountRepo().get().collect { accounts ->
                _state.update { it.copy(accounts = accounts.map { dbAccount -> Account(dbAccount) }) }
            }
        }
    }

    fun handle(action: TransactionsSummaryIntent) {
        when (action) {
            is NavigationTabClicked -> _state.update { it.copy(currentScreen = action.tab) }
            is FilterIntent -> handleFilterIntent(action)
            is SummaryIntent -> handleSummaryIntent(action)
            is SelectDialogIntent -> handleSelectDialogIntent(action)
        }
    }

    private fun handleFilterIntent(action: FilterIntent) {
        when (action) {
            is FromDateChanged -> updateFilterState { it.copy(from = action.date) }
            is ToDateChanged -> updateFilterState { it.copy(to = action.date) }
            ToggleShowIncoming -> updateFilterState { it.copy(showIncoming = !it.showIncoming) }
            ToggleShowOutgoing -> updateFilterState { it.copy(showOutgoing = !it.showOutgoing) }
            ToggleOutgoingIsPositive -> _state.update { it.copy(outgoingIsPositive = !it.outgoingIsPositive) }
        }
    }

    private fun updateFilterState(update: (TransactionsFilters) -> TransactionsFilters) =
            _state.update { it.copy(filtersState = update(it.filtersState)) }

    private fun handleSummaryIntent(action: SummaryIntent) {
        when (action) {
            is SummaryItemSelected ->
                _state.update {
                    val newItem = action.index.takeIf { newIndex -> newIndex != it.selectedItemIndex }
                    it.copy(selectedItemIndex = newItem)
                }
            is SummaryPieClicked -> {
                val degrees = Math.toDegrees(action.polar.theta.toDouble()).toFloat()
                _state.update {
                    val newItem = it.getSegmentIndex(degrees).takeIf { newIndex -> newIndex != it.selectedItemIndex }
                    it.copy(selectedItemIndex = newItem)
                }
            }
            is ViewTransactionsAction -> handleViewTransactionIntent(action.action)
        }
    }

    private fun handleSelectDialogIntent(action: SelectDialogIntent) {
        when (action) {
            is OpenSelectDialog ->
                _state.update {
                    it.copy(
                            openSelectDialog = action.type,
                            selectedDialogIds = when (action.type) {
                                ACCOUNT -> it.filtersState.selectedAccountIds
                                CATEGORY -> it.filtersState.selectedCategoryIds
                                PERSON -> it.filtersState.selectedPersonIds
                                else -> emptyList()
                            },
                    )
                }
            is SelectDialogAction -> when (action.action) {
                SelectItemDialogIntent.Close ->
                    _state.update { it.copy(selectedDialogIds = emptyList(), openSelectDialog = null) }
                SelectItemDialogIntent.CreateNew -> throw NotImplementedError()
                is SelectItemDialogIntent.ToggleItemSelected<*> ->
                    _state.update {
                        it.copy(selectedDialogIds = it.selectedDialogIds.toggle(action.action.item?.getItemId()))
                    }
                is SelectItemDialogIntent.ItemChosen<*> ->
                    _state.update {
                        if (it.openSelectDialog?.isMultiSelectable != false) return
                        val newState = it.copy(
                                selectedDialogIds = emptyList(),
                                openSelectDialog = null,
                        )
                        when (it.openSelectDialog) {
                            GROUP -> newState.copy(currentGrouping = action.action.item as TransactionsSummaryGrouping)
                            else -> throw NotImplementedError()
                        }
                    }
                SelectItemDialogIntent.Submit ->
                    _state.update {
                        if (it.openSelectDialog?.isMultiSelectable != true) return
                        val newFilterState = when (it.openSelectDialog) {
                            ACCOUNT -> it.filtersState.copy(selectedAccountIds = it.selectedDialogIds)
                            CATEGORY -> it.filtersState.copy(selectedCategoryIds = it.selectedDialogIds)
                            PERSON -> it.filtersState.copy(selectedPersonIds = it.selectedDialogIds)
                            else -> throw NotImplementedError()
                        }
                        it.copy(
                                selectedDialogIds = emptyList(),
                                openSelectDialog = null,
                                filtersState = newFilterState,
                        )
                    }
            }
        }
    }
}
