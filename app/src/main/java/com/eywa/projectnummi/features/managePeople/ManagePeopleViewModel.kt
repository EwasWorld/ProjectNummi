package com.eywa.projectnummi.features.managePeople

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.components.createPersonDialog.CreatePersonDialogIntent
import com.eywa.projectnummi.components.createPersonDialog.CreatePersonDialogState
import com.eywa.projectnummi.database.TempInMemoryDb
import com.eywa.projectnummi.features.managePeople.ManagePeopleIntent.AddPersonClicked
import com.eywa.projectnummi.features.managePeople.ManagePeopleIntent.CreatePersonDialogAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManagePeopleViewModel : ViewModel() {
    private val _state = MutableStateFlow(ManagePeopleState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            TempInMemoryDb.people.collect { people ->
                _state.update { it.copy(people = people) }
            }
        }
    }

    fun handle(action: ManagePeopleIntent) {
        when (action) {
            AddPersonClicked -> _state.update { it.copy(createDialogState = CreatePersonDialogState()) }
            is CreatePersonDialogAction -> handleCreateDialogIntent(action.action)
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
                val person = _state.value.createDialogState?.asPerson() ?: return
                _state.update { it.copy(createDialogState = null) }

                viewModelScope.launch { TempInMemoryDb.addPerson(person) }
            }
        }
    }
}
