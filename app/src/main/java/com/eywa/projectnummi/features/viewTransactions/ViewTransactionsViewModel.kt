package com.eywa.projectnummi.features.viewTransactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.database.transaction.TransactionRepo
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.navigation.NummiNavArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewTransactionsViewModel @Inject constructor(
        db: NummiDatabase,
        savedStateHandle: SavedStateHandle,
) : ViewModel(), ViewTransactionsViewModelHelper {
    private val transactionRepo = db.transactionRepo()

    private val _state = MutableStateFlow(ViewTransactionsState())
    val state = _state.asStateFlow()

    override val isRecurring: Boolean =
            savedStateHandle.get<Boolean>(NummiNavArgument.RECURRING_TRANSACTIONS.toArgName()) ?: false

    override val vtTransactionRepo: TransactionRepo = transactionRepo
    override val vtViewModelScope: CoroutineScope = viewModelScope

    override fun updateState(block: (ViewTransactionsState) -> ViewTransactionsState) = _state.update(block)
    override fun currentState(): ViewTransactionsState = _state.value

    override fun getContextMenuItems(): List<ViewTransactionsManageItemOptions> = listOfNotNull(
            ViewTransactionsManageItemOptions.Add.takeIf { isRecurring },
            ViewTransactionsManageItemOptions.Edit,
            ViewTransactionsManageItemOptions.MoveUp.takeIf { !isRecurring },
            ViewTransactionsManageItemOptions.MoveDown.takeIf { !isRecurring },
            ViewTransactionsManageItemOptions.Delete,
    )

    init {
        vtViewModelScope.launch {
            transactionRepo.getFull(isRecurring).collect { dbTransactions ->
                _state.update {
                    dbTransactions
                            .map { dbTransaction -> Transaction(dbTransaction) }
                            .let { transactions ->
                                if (isRecurring) transactions.sortedBy { transaction -> transaction.name }
                                else transactions
                            }
                            .let { transactions -> it.copy(transactions = transactions) }
                }
            }
        }
    }
}

