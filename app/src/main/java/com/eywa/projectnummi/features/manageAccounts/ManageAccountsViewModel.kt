package com.eywa.projectnummi.features.manageAccounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.features.manageAccounts.ManageAccountsIntent.*
import com.eywa.projectnummi.model.objects.Account
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialogIntent
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialogState
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDefaultOption
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState
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

    private val contextMenuOptions by lazy {
        mapOf(
                ManageItemDefaultOption.EDIT to {
                    _state.update {
                        it.copy(
                                manageItemDialogState = null,
                                createDialogState = it.manageItemDialogState?.item
                                        ?.let { account -> CreateAccountDialogState(editing = account) }
                        )
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
            accountRepo.get().collect { accounts ->
                _state.update { it.copy(accounts = accounts.map { dbAccount -> Account(dbAccount) }) }
            }
        }
    }

    fun handle(action: ManageAccountsIntent) {
        when (action) {
            is AccountClicked ->
                _state.update {
                    it.copy(
                            manageItemDialogState = ManageItemDialogState(
                                    item = action.account,
                                    options = contextMenuOptions.keys.toList(),
                            )
                    )
                }
            AddAccountClicked -> _state.update { it.copy(createDialogState = CreateAccountDialogState()) }
            is CreateAccountDialogAction -> handleCreateDialogIntent(action.action)
            is ManageItemDialogAction -> handleManageItemDialogIntent(action.action)
            is DeleteConfirmationDialogAction -> handleDeleteConfirmationDialogIntent(action.action)
            is TabClicked -> _state.update { it.copy(navigateInitiatedFor = action.item.navRoute) }
            NavigationResolved -> _state.update { it.copy(navigateInitiatedFor = null) }
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
                val dialogState = _state.value.createDialogState ?: return
                _state.update { it.copy(createDialogState = null) }

                val account = dialogState.asAccount().asDbAccount()
                viewModelScope.launch {
                    if (dialogState.isEditing) {
                        accountRepo.update(account)
                    }
                    else {
                        accountRepo.insert(account)
                    }
                }
            }
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
                    viewModelScope.launch { accountRepo.delete(deleteItem.asDbAccount()) }
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
