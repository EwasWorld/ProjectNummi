package com.eywa.projectnummi.features.manageCategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.components.category.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.components.category.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.AddCategoryClicked
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.CreateCategoryDialogAction
import com.eywa.projectnummi.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(
        db: NummiDatabase,
) : ViewModel() {
    private val categoryRepo = db.categoryRepo()

    private val _state = MutableStateFlow(ManageCategoriesState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepo.get().collect { categories ->
                _state.update { it.copy(categories = categories.map { dbCat -> Category(dbCat) }) }
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

                viewModelScope.launch { categoryRepo.insert(category.asDbCategory()) }
            }
        }
    }
}
