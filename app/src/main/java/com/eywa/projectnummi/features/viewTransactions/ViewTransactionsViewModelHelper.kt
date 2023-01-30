package com.eywa.projectnummi.features.viewTransactions

import com.eywa.projectnummi.database.transaction.TransactionRepo
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface ViewTransactionsViewModelHelper {
    val isRecurring: Boolean
    val vtTransactionRepo: TransactionRepo
    val vtViewModelScope: CoroutineScope

    fun updateState(block: (ViewTransactionsState) -> ViewTransactionsState)
    fun currentState(): ViewTransactionsState?

    fun getContextMenuItems(): List<ViewTransactionsManageItemOptions>

    fun handleViewTransactionIntent(action: ViewTransactionsIntent) {
        when (action) {
            is ViewTransactionsIntent.TransactionClicked ->
                updateState {
                    val newId = action.transaction.id.takeIf { id -> id != it.selectedTransactionId }
                    it.copy(selectedTransactionId = newId)
                }
            is ViewTransactionsIntent.TransactionLongClicked ->
                updateState {
                    val items = getContextMenuItems().filter { item -> item.shouldShow(it, action.transaction) }
                    it.copy(manageItemDialogState = ManageItemDialogState(action.transaction, items))
                }
            is ViewTransactionsIntent.ManageItemDialogAction -> handleManageItemDialogIntent(action.action)
            is ViewTransactionsIntent.DeleteConfirmationDialogAction -> handleDeleteConfirmationDialogIntent(action.action)
            is ViewTransactionsIntent.TabClicked -> updateState {
                it.copy(
                        extras = it.extras.plusOrReplace(
                                ViewTransactionsExtra.ManageTabChanged(action.item)
                        )
                )
            }
            ViewTransactionsIntent.AddClicked -> updateState {
                it.copy(
                        extras = it.extras.plusOrReplace(
                                ViewTransactionsExtra.AddTransactions
                        )
                )
            }
            is ViewTransactionsIntent.ClearExtra -> updateState { it.copy(extras = it.extras.minus(action.extra::class)) }
        }
    }

    private fun handleManageItemDialogIntent(action: ManageItemDialogIntent) {
        when (action) {
            ManageItemDialogIntent.Close -> updateState { it.copy(manageItemDialogState = null) }
            is ManageItemDialogIntent.OptionClicked -> when (action.option) {
                ViewTransactionsManageItemOptions.Add ->
                    updateState {
                        val item = it.manageItemDialogState?.item ?: return@updateState it
                        it.copy(
                                manageItemDialogState = null,
                                extras = it.extras.plusOrReplace(ViewTransactionsExtra.NewTransactionFromRecurring(item)),
                        )
                    }
                ViewTransactionsManageItemOptions.Edit ->
                    updateState {
                        val item = it.manageItemDialogState?.item ?: return@updateState it
                        it.copy(
                                manageItemDialogState = null,
                                extras = it.extras.plusOrReplace(ViewTransactionsExtra.EditTransaction(item)),
                        )
                    }
                ViewTransactionsManageItemOptions.MoveUp ->
                    vtViewModelScope.launch {
                        val item = currentState()?.manageItemDialogState?.item?.asDbTransaction()
                        if (item != null) vtTransactionRepo.decreaseOrder(item)
                        updateState { it.copy(manageItemDialogState = null) }
                    }
                ViewTransactionsManageItemOptions.MoveDown ->
                    vtViewModelScope.launch {
                        val item = currentState()?.manageItemDialogState?.item?.asDbTransaction()
                        if (item != null) vtTransactionRepo.increaseOrder(item)
                        updateState { it.copy(manageItemDialogState = null) }
                    }
                ViewTransactionsManageItemOptions.Delete ->
                    updateState {
                        it.copy(
                                deleteDialogState = it.manageItemDialogState?.item
                                        ?.let { item -> DeleteConfirmationDialogState(item) }
                        )
                    }
            }
        }
    }

    private fun handleDeleteConfirmationDialogIntent(action: DeleteConfirmationDialogIntent) {
        when (action) {
            DeleteConfirmationDialogIntent.Ok -> {
                val deleteItem = currentState()?.deleteDialogState?.item
                if (deleteItem != null) {
                    vtViewModelScope.launch { vtTransactionRepo.delete(deleteItem.asDbTransaction()) }
                }
                updateState {
                    it.copy(
                            manageItemDialogState = null,
                            deleteDialogState = null,
                    )
                }
            }
            DeleteConfirmationDialogIntent.Cancel -> updateState { it.copy(deleteDialogState = null) }
        }
    }
}
