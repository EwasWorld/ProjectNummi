package com.eywa.projectnummi.features.manageCategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.database.TempInMemoryDb
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.AddCategoryClicked
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.CreateCategoryDialogAction
import com.eywa.projectnummi.model.Category
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
            CreateCategoryDialogIntent.Close -> _state.update { it.copy(createDialogState = null) }
            is CreateCategoryDialogIntent.NameChanged ->
                _state.update { it.copy(createDialogState = it.createDialogState?.copy(name = action.name)) }
            CreateCategoryDialogIntent.Submit -> {
                val oldState = _state.value.createDialogState!!
                _state.update { it.copy(createDialogState = null) }

                viewModelScope.launch {
                    TempInMemoryDb.addCategory(
                            Category(
                                    id = 0,
                                    name = oldState.name,
                            )
                    )
                }
            }
        }
    }
}
