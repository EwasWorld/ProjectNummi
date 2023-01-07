package com.eywa.projectnummi.features.managePeople

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.components.person.createPersonDialog.CreatePersonDialogIntent
import com.eywa.projectnummi.components.person.createPersonDialog.CreatePersonDialogState
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.features.managePeople.ManagePeopleIntent.AddPersonClicked
import com.eywa.projectnummi.features.managePeople.ManagePeopleIntent.CreatePersonDialogAction
import com.eywa.projectnummi.model.Person
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

    init {
        viewModelScope.launch {
            personRepo.get().collect { people ->
                _state.update { it.copy(people = people.map { dbPerson -> Person(dbPerson) }) }
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

                viewModelScope.launch { personRepo.insert(person.asDbPerson()) }
            }
        }
    }
}
