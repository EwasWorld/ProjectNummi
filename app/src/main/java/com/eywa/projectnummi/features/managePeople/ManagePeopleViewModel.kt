package com.eywa.projectnummi.features.managePeople

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.features.managePeople.ManagePeopleIntent.*
import com.eywa.projectnummi.model.objects.Person
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogIntent
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDefaultOption
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogIntent
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialogIntent
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagePeopleViewModel @Inject constructor(
        db: NummiDatabase,
) : ViewModel() {
    private val personRepo = db.personRepo()

    private val _state = MutableStateFlow(ManagePeopleState())
    val state = _state.asStateFlow()

    private val contextMenuOptions by lazy {
        mapOf(
                ManageItemDefaultOption.EDIT to {
                    _state.update {
                        it.copy(
                                manageItemDialogState = null,
                                createDialogState = it.manageItemDialogState?.item
                                        ?.let { person -> CreatePersonDialogState(editing = person) }
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
            personRepo.get().collect { people ->
                _state.update { it.copy(people = people.map { dbPerson -> Person(dbPerson) }) }
            }
        }
    }

    fun handle(action: ManagePeopleIntent) {
        when (action) {
            is PersonClicked ->
                _state.update {
                    it.copy(
                            manageItemDialogState = ManageItemDialogState(
                                    item = action.person,
                                    options = contextMenuOptions.keys.toList(),
                            )
                    )
                }
            AddPersonClicked -> _state.update { it.copy(createDialogState = CreatePersonDialogState()) }
            is CreatePersonDialogAction -> handleCreateDialogIntent(action.action)
            is ManageItemDialogAction -> handleManageItemDialogIntent(action.action)
            is DeleteConfirmationDialogAction -> handleDeleteConfirmationDialogIntent(action.action)
            is TabClicked -> _state.update { it.copy(navigateInitiatedFor = action.item.navRoute) }
            NavigationResolved -> _state.update { it.copy(navigateInitiatedFor = null) }
        }
    }

    private fun handleCreateDialogIntent(action: CreatePersonDialogIntent) {
        when (action) {
            is CreatePersonDialogIntent.NameChanged ->
                _state.update {
                    val currentState = it.createDialogState ?: return
                    it.copy(createDialogState = action.updateState(currentState))
                }
            CreatePersonDialogIntent.Close -> _state.update { it.copy(createDialogState = null) }
            CreatePersonDialogIntent.Submit -> {
                val dialogState = _state.value.createDialogState ?: return
                _state.update { it.copy(createDialogState = null) }

                val person = dialogState.asPerson().asDbPerson()
                viewModelScope.launch {
                    if (dialogState.isEditing) {
                        personRepo.update(person)
                    }
                    else {
                        personRepo.insert(person)
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
                    viewModelScope.launch { personRepo.delete(deleteItem.asDbPerson()) }
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
