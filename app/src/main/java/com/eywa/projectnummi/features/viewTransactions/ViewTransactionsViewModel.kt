package com.eywa.projectnummi.features.viewTransactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsExtra.*
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsIntent.*
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.navigation.NummiNavArgument
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDefaultOption
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewTransactionsViewModel @Inject constructor(
        db: NummiDatabase,
        savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val transactionRepo = db.transactionRepo()

    private val _state = MutableStateFlow(
            ViewTransactionsState(
                    isRecurring = savedStateHandle
                            .get<Boolean>(NummiNavArgument.RECURRING_TRANSACTIONS.toArgName()) ?: false
            )
    )
    val state = _state.asStateFlow()

    private val contextMenuOptions: Map<ManageItemOption, () -> Unit> by lazy {
        mapOf(
                ViewTransactionsManageItemOptions.ADD to {
                    _state.update {
                        val item = it.manageItemDialogState?.item ?: return@update it
                        it.copy(
                                manageItemDialogState = null,
                                extras = it.extras.plusOrReplace(NewTransactionFromRecurring(item)),
                        )
                    }
                },
                ManageItemDefaultOption.EDIT to {
                    _state.update {
                        val item = it.manageItemDialogState?.item ?: return@update it
                        it.copy(
                                manageItemDialogState = null,
                                extras = it.extras.plusOrReplace(EditTransaction(item)),
                        )
                    }
                },
                ViewTransactionsManageItemOptions.MOVE_UP to {
                    viewModelScope.launch {
                        val item = _state.value.manageItemDialogState?.item?.asDbTransaction()
                        if (item != null) transactionRepo.decreaseOrder(item)
                        _state.update { it.copy(manageItemDialogState = null) }
                    }
                },
                ViewTransactionsManageItemOptions.MOVE_DOWN to {
                    viewModelScope.launch {
                        val item = _state.value.manageItemDialogState?.item?.asDbTransaction()
                        if (item != null) transactionRepo.increaseOrder(item)
                        _state.update { it.copy(manageItemDialogState = null) }
                    }
                },
                ManageItemDefaultOption.DELETE to {
                    _state.update {
                        it.copy(
                                deleteDialogState = it.manageItemDialogState?.item
                                        ?.let { item -> DeleteConfirmationDialogState(item) }
                        )
                    }
                },
        )
    }

    init {
        viewModelScope.launch {
            transactionRepo.getFull(_state.value.isRecurring).collect { transactions ->
                _state.update { it.copy(transactions = transactions.map { dbTrans -> Transaction(dbTrans) }) }
            }
        }
    }

    fun handle(action: ViewTransactionsIntent) {
        when (action) {
            is TransactionClicked ->
                _state.update {
                    it.copy(
                            manageItemDialogState = ManageItemDialogState(
                                    item = action.transaction,
                                    options = contextMenuOptions.keys.toList().filter { option ->
                                        (option as? ViewTransactionsManageItemOptions)
                                                ?.shouldShow?.invoke(it, action.transaction)
                                                ?: true
                                    },
                            )
                    )
                }
            is ManageItemDialogAction -> handleManageItemDialogIntent(action.action)
            is DeleteConfirmationDialogAction -> handleDeleteConfirmationDialogIntent(action.action)
            is TabClicked -> _state.update { it.copy(extras = it.extras.plusOrReplace(ManageTabChanged(action.item))) }
            AddClicked -> _state.update { it.copy(extras = it.extras.plusOrReplace(AddTransactions)) }
            is ClearExtra -> _state.update { it.copy(extras = it.extras.minus(action.extra::class)) }
        }
    }

    private fun handleManageItemDialogIntent(action: ManageItemDialogIntent) {
        when (action) {
            ManageItemDialogIntent.Close -> _state.update { it.copy(manageItemDialogState = null) }
            is ManageItemDialogIntent.OptionClicked ->
                contextMenuOptions[action.option]?.invoke() ?: throw NotImplementedError()
        }
    }

    private fun handleDeleteConfirmationDialogIntent(action: DeleteConfirmationDialogIntent) {
        when (action) {
            DeleteConfirmationDialogIntent.Ok -> {
                val deleteItem = _state.value.deleteDialogState?.item
                if (deleteItem != null) {
                    viewModelScope.launch { transactionRepo.delete(deleteItem.asDbTransaction()) }
                }
                _state.update {
                    it.copy(
                            manageItemDialogState = null,
                            deleteDialogState = null,
                    )
                }
            }
            DeleteConfirmationDialogIntent.Cancel -> _state.update { it.copy(deleteDialogState = null) }
        }
    }
}

private enum class ViewTransactionsManageItemOptions(
        override val text: String,
        val shouldShow: (state: ViewTransactionsState, selectedItem: Transaction) -> Boolean,
) : ManageItemOption {
    MOVE_UP("Move up", { state, selectedItem ->
        val matchingDate = state.transactions.filter { it.date == selectedItem.date }
        !state.isRecurring && matchingDate.size > 1 && selectedItem.order != matchingDate.maxOfOrNull { it.order }
    }),
    MOVE_DOWN("Move down", { state, selectedItem ->
        val matchingDate = state.transactions.filter { it.date == selectedItem.date }
        !state.isRecurring && matchingDate.size > 1 && selectedItem.order != matchingDate.minOfOrNull { it.order }
    }),
    ADD("Add", { state, _ -> state.isRecurring })
}
