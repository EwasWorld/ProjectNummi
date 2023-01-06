package com.eywa.projectnummi.features.addTransactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.common.div100String
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.components.createPersonDialog.CreatePersonDialogIntent
import com.eywa.projectnummi.components.createPersonDialog.CreatePersonDialogState
import com.eywa.projectnummi.components.selectCategoryDialog.SelectCategoryDialogIntent
import com.eywa.projectnummi.components.selectPersonDialog.SelectPersonDialogIntent
import com.eywa.projectnummi.database.TempInMemoryDb
import com.eywa.projectnummi.features.addTransactions.AddTransactionsIntent.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class AddTransactionsViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddTransactionsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            TempInMemoryDb.categories.collect { categories ->
                _state.update { it.copy(categories = categories) }
            }
        }
        viewModelScope.launch {
            TempInMemoryDb.people.collect { people ->
                _state.update { it.copy(people = people) }
            }
        }
    }

    fun handle(action: AddTransactionsIntent) {
        when (action) {
            is ValueChangedIntent -> handleValueChangedIntent(action)

            is StartChangeCategory -> _state.update {
                if (it.categories == null || it.categories.isNotEmpty()) {
                    it.copy(currentRow = action.rowIndex, selectCategoryDialogIsShown = true)
                }
                else {
                    it.copy(currentRow = action.rowIndex, createCategoryDialogState = CreateCategoryDialogState())
                }
            }
            is CreateCategoryDialogAction -> handleCreateCategoryIntent(action.action)
            is SelectCategoryDialogAction -> handleSelectCategoryIntent(action.action)

            is StartChangePerson -> openSelectPersonDialog(action.rowIndex, SelectPersonDialogPurpose.SELECT)
            is CreatePersonDialogAction -> handleCreatePersonIntent(action.action)
            is SelectPersonDialogAction -> handleSelectPersonIntent(action.action)

            Clear -> _state.update {
                AddTransactionsState(
                        categories = it.categories,
                        people = it.people,
                )
            }
            Submit -> viewModelScope.launch {
                val oldState = state.value
                if (oldState.name.isBlank()) return@launch

                TempInMemoryDb.addTransaction(oldState.asTransaction())
                _state.update {
                    it.copy(
                            name = "",
                            isOutgoing = true,
                            amountRows = listOf(AmountInputState()),
                    )
                }
            }

            AddAmountRow ->
                _state.update { it.copy(amountRows = it.amountRows.plus(it.amountRows.last().copy(amount = ""))) }
            is DeleteAmountRow ->
                _state.update {
                    val rows = it.amountRows
                    it.copy(amountRows = rows.take(action.rowIndex).plus(rows.drop(action.rowIndex + 1)))
                }
            Split -> {
                if (_state.value.amountRows.size > 1) return
                openSelectPersonDialog(null, SelectPersonDialogPurpose.SPLIT)
            }
        }
    }

    private fun openSelectPersonDialog(rowIndex: Int?, select: SelectPersonDialogPurpose) {
        require(rowIndex != null || select != SelectPersonDialogPurpose.SELECT) {
            "rowIndex cannot be null for SELECT purpose"
        }

        _state.update {
            if (it.people == null || it.people.isNotEmpty()) {
                it.copy(currentRow = rowIndex, selectPersonDialogIsShown = select)
            }
            else {
                it.copy(currentRow = rowIndex, createPersonDialogState = CreatePersonDialogState())
            }
        }
    }

    private fun handleValueChangedIntent(action: ValueChangedIntent) {
        when (action) {
            is DateIncremented -> {
                _state.update {
                    val dateCopy = it.date.clone() as Calendar
                    it.copy(date = (dateCopy.apply { add(Calendar.DATE, action.daysAdded) }))
                }
            }
            is DateChanged -> _state.update { it.copy(date = action.date) }
            is AmountChanged ->
                _state.update {
                    it.copy(amountRows = it.amountRows.updateAt(action.rowIndex) { copy(amount = action.amount) })
                }
            is NameChanged -> _state.update { it.copy(name = action.name) }
            ToggleIsOutgoing -> _state.update { it.copy(isOutgoing = !it.isOutgoing) }
        }
    }

    private fun List<AmountInputState>.updateAt(index: Int, mutator: AmountInputState.() -> AmountInputState) =
            take(index)
                    .plus(get(index).mutator())
                    .plus(drop(index + 1))

    private fun handleCreateCategoryIntent(action: CreateCategoryDialogIntent) {
        when (action) {
            is CreateCategoryDialogIntent.HueChanged,
            is CreateCategoryDialogIntent.NameChanged,
            ->
                _state.update {
                    val createState = it.createCategoryDialogState ?: return
                    it.copy(createCategoryDialogState = action.updateState(createState))
                }
            CreateCategoryDialogIntent.Close -> _state.update { it.copy(createCategoryDialogState = null) }
            CreateCategoryDialogIntent.Submit -> {
                val category = _state.value.createCategoryDialogState?.asCategory() ?: return
                viewModelScope.launch {
                    val id = TempInMemoryDb.addCategory(category)
                    _state.update { it.setCategoryId(id) }
                }
            }
        }
    }

    private fun AddTransactionsState.setPersonId(id: Int?) = updateRowAndCloseDialogs { copy(personId = id) }
    private fun AddTransactionsState.setCategoryId(id: Int?) = updateRowAndCloseDialogs { copy(categoryId = id) }

    private fun AddTransactionsState.updateRowAndCloseDialogs(mutator: AmountInputState.() -> AmountInputState) =
            copy(
                    amountRows = _state.value.currentRow?.let { amountRows.updateAt(it, mutator) }
                            ?: amountRows,
            ).closeDialogs()

    private fun AddTransactionsState.closeDialogs() =
            copy(
                    createCategoryDialogState = null,
                    selectCategoryDialogIsShown = false,
                    createPersonDialogState = null,
                    selectPersonDialogIsShown = null,
                    currentRow = null,
            )

    private fun handleSelectCategoryIntent(action: SelectCategoryDialogIntent) {
        when (action) {
            SelectCategoryDialogIntent.Close -> _state.update { it.copy(selectCategoryDialogIsShown = false) }
            is SelectCategoryDialogIntent.CategoryClicked -> _state.update { it.setCategoryId(action.category.id) }
            is SelectCategoryDialogIntent.NoCategoryClicked -> _state.update { it.setCategoryId(null) }
            SelectCategoryDialogIntent.CreateNew ->
                _state.update {
                    it.copy(
                            selectCategoryDialogIsShown = false,
                            createCategoryDialogState = CreateCategoryDialogState(),
                    )
                }
        }
    }

    private fun handleCreatePersonIntent(action: CreatePersonDialogIntent) {
        when (action) {
            is CreatePersonDialogIntent.NameChanged ->
                _state.update {
                    val createState = it.createPersonDialogState ?: return
                    it.copy(createPersonDialogState = action.updateState(createState))
                }
            CreatePersonDialogIntent.Close -> _state.update { it.copy(createPersonDialogState = null) }
            CreatePersonDialogIntent.Submit -> {
                val person = _state.value.createPersonDialogState?.asPerson() ?: return
                viewModelScope.launch {
                    val id = TempInMemoryDb.addPerson(person)
                    // Create person can only be triggered from the select person action
                    //      so after the person is created, select them
                    handleSelectPersonIntent(
                            SelectPersonDialogIntent.PersonClicked(person.copy(id = id))
                    )
                }
            }
        }
    }

    private fun handleSelectPersonIntent(action: SelectPersonDialogIntent) {
        when (action) {
            SelectPersonDialogIntent.Close -> _state.update { it.copy(selectPersonDialogIsShown = null) }
            is SelectPersonDialogIntent.PersonClicked ->
                _state.update {
                    if (it.selectPersonDialogIsShown == SelectPersonDialogPurpose.SELECT) {
                        it.setPersonId(action.person?.id)
                    }
                    else {
                        val people = listOf(
                                it.amountRows[0].personId,
                                action.person?.id
                        )
                        val floorAmount = it.totalAmount / people.size
                        val remainder = it.totalAmount - floorAmount * people.size
                        val amounts = List(people.size) { index ->
                            floorAmount + if (index < remainder) 1 else 0
                        }.shuffled()
                        it.copy(
                                amountRows = people
                                        .mapIndexed { index, personId ->
                                            it.amountRows[0].copy(
                                                    amount = amounts[index].div100String(),
                                                    personId = personId
                                            )
                                        }
                        ).closeDialogs()
                    }
                }
            SelectPersonDialogIntent.CreateNew ->
                _state.update {
                    it.copy(
                            selectPersonDialogIsShown = null,
                            createPersonDialogState = CreatePersonDialogState(),
                    )
                }
        }
    }
}
