package com.eywa.projectnummi.features.viewTransactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.components.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDefaultOption
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.components.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.components.manageItemDialog.ManageItemOption
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.features.viewTransactions.ViewTransactionsIntent.*
import com.eywa.projectnummi.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewTransactionsViewModel @Inject constructor(
        db: NummiDatabase,
) : ViewModel() {
    private val transactionRepo = db.transactionRepo()

    private val _state = MutableStateFlow(ViewTransactionsState())
    val state = _state.asStateFlow()

    private val contextMenuOptions: Map<ManageItemOption, () -> Unit> by lazy {
        mapOf(
                ManageItemDefaultOption.EDIT to {
                    _state.update {
                        it.copy(
                                manageItemDialogState = null,
                                editTransactionInitiatedFor = it.manageItemDialogState?.item,
                        )
                    }
                },
                ViewTransactionsManageItemOptions.MOVE_UP to {
                    viewModelScope.launch {
                        val item = _state.value.manageItemDialogState?.item?.asDbTransaction()
                        if (item != null) transactionRepo.increaseOrder(item)
                        _state.update { it.copy(manageItemDialogState = null) }
                    }
                },
                ViewTransactionsManageItemOptions.MOVE_DOWN to {
                    viewModelScope.launch {
                        val item = _state.value.manageItemDialogState?.item?.asDbTransaction()
                        if (item != null) transactionRepo.decreaseOrder(item)
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
            transactionRepo.getFull().collect { transactions ->
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
            NavigatedToEditItem -> _state.update { it.copy(editTransactionInitiatedFor = null) }
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
        selectedItem.order != state.transactions.minOfOrNull { it.order }
    }),
    MOVE_DOWN("Move down", { state, selectedItem ->
        selectedItem.order != state.transactions.maxOfOrNull { it.order }
    }),
}
