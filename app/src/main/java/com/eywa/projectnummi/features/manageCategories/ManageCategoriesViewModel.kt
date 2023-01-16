package com.eywa.projectnummi.features.manageCategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.features.manageCategories.ManageCategoriesIntent.*
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogState
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
class ManageCategoriesViewModel @Inject constructor(
        db: NummiDatabase,
) : ViewModel() {
    private val categoryRepo = db.categoryRepo()

    private val _state = MutableStateFlow(ManageCategoriesState())
    val state = _state.asStateFlow()

    private val contextMenuOptions by lazy {
        mapOf(
                ManageItemDefaultOption.EDIT to {
                    _state.update {
                        it.copy(
                                manageItemDialogState = null,
                                createDialogState = it.manageItemDialogState?.item
                                        ?.let { category -> CreateCategoryDialogState(editing = category) }
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
            categoryRepo.get().collect { categories ->
                _state.update { it.copy(categories = categories.map { dbCat -> Category(dbCat) }) }
            }
        }
    }

    fun handle(action: ManageCategoriesIntent) {
        when (action) {
            is CategoryClicked ->
                _state.update {
                    it.copy(
                            manageItemDialogState = ManageItemDialogState(
                                    item = action.category,
                                    options = contextMenuOptions.keys.toList(),
                            )
                    )
                }
            AddCategoryClicked -> _state.update { it.copy(createDialogState = CreateCategoryDialogState()) }
            is CreateCategoryDialogAction -> handleCreateDialogIntent(action.action)
            is ManageItemDialogAction -> handleManageItemDialogIntent(action.action)
            is DeleteConfirmationDialogAction -> handleDeleteConfirmationDialogIntent(action.action)
            is TabClicked -> _state.update { it.copy(navigateInitiatedFor = action.item.navRoute) }
            NavigationResolved -> _state.update { it.copy(navigateInitiatedFor = null) }
        }
    }

    private fun handleCreateDialogIntent(action: CreateCategoryDialogIntent) {
        when (action) {
            is CreateCategoryDialogIntent.NameChanged,
            is CreateCategoryDialogIntent.HueChanged,
            ->
                _state.update {
                    val currentState = it.createDialogState ?: return
                    it.copy(createDialogState = action.updateState(currentState))
                }
            CreateCategoryDialogIntent.Close -> _state.update { it.copy(createDialogState = null) }
            CreateCategoryDialogIntent.Submit -> {
                val dialogState = _state.value.createDialogState ?: return
                _state.update { it.copy(createDialogState = null) }

                val category = dialogState.asCategory().asDbCategory()
                viewModelScope.launch {
                    if (dialogState.isEditing) {
                        categoryRepo.update(category)
                    }
                    else {
                        categoryRepo.insert(category)
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
                    viewModelScope.launch { categoryRepo.delete(deleteItem.asDbCategory()) }
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
