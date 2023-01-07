package com.eywa.projectnummi.features.manageAccounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.components.createAccountDialog.CreateAccountDialogIntent
import com.eywa.projectnummi.components.createAccountDialog.CreateAccountDialogState
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.features.manageAccounts.ManageAccountsIntent.AddAccountClicked
import com.eywa.projectnummi.features.manageAccounts.ManageAccountsIntent.CreateAccountDialogAction
import com.eywa.projectnummi.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAccountsViewModel @Inject constructor(
        db: NummiDatabase,
) : ViewModel() {
    private val accountRepo = db.accountRepo()

    private val _state = MutableStateFlow(ManageAccountsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            accountRepo.get().collect { accounts ->
                _state.update { it.copy(accounts = accounts.map { dbAccount -> Account(dbAccount) }) }
            }
        }
    }

    fun handle(action: ManageAccountsIntent) {
        when (action) {
            AddAccountClicked -> _state.update { it.copy(createDialogState = CreateAccountDialogState()) }
            is CreateAccountDialogAction -> handleCreateDialogIntent(action.action)
        }
    }

    private fun handleCreateDialogIntent(action: CreateAccountDialogIntent) {
        when (action) {
            is CreateAccountDialogIntent.TypeChanged,
            is CreateAccountDialogIntent.NameChanged,
            ->
                _state.update {
                    val currentState = it.createDialogState ?: return
                    it.copy(createDialogState = action.updateState(currentState))
                }
            CreateAccountDialogIntent.Close -> _state.update { it.copy(createDialogState = null) }
            CreateAccountDialogIntent.Submit -> {
                val account = _state.value.createDialogState?.asAccount() ?: return
                _state.update { it.copy(createDialogState = null) }

                viewModelScope.launch { accountRepo.insert(account.asDbAccount()) }
            }
        }
    }
}
