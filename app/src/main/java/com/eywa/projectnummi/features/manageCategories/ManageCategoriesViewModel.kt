package com.eywa.projectnummi.features.manageCategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.database.TempInMemoryDb
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.AddCategoryClicked
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.CreateCategoryDialogAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageCategoriesViewModel : ViewModel() {
    private val _state = MutableStateFlow(ManageCategoriesState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            TempInMemoryDb.categories.collect { categories ->
                _state.update { it.copy(categories = categories) }
            }
        }
    }

    fun handle(action: ManageCategoriesIntent) {
        when (action) {
            AddCategoryClicked -> _state.update { it.copy(createDialogState = CreateCategoryDialogState()) }
            is CreateCategoryDialogAction -> handleCreateDialogIntent(action.action)
        }
    }

    private fun handleCreateDialogIntent(action: CreateCategoryDialogIntent) {
        when (action) {
            is CreateCategoryDialogIntent.NameChanged,
            is CreateCategoryDialogIntent.HueChanged ->
                _state.update {
                    val currentState = it.createDialogState ?: return
                    it.copy(createDialogState = action.updateState(currentState))
                }
            CreateCategoryDialogIntent.Close -> _state.update { it.copy(createDialogState = null) }
            CreateCategoryDialogIntent.Submit -> {
                val category = _state.value.createDialogState?.asCategory() ?: return
                _state.update { it.copy(createDialogState = null) }

                viewModelScope.launch { TempInMemoryDb.addCategory(category) }
            }
        }
    }
}