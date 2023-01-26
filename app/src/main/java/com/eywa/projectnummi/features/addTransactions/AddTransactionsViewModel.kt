package com.eywa.projectnummi.features.addTransactions

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eywa.projectnummi.database.NummiDatabase
import com.eywa.projectnummi.features.addTransactions.AddTransactionsIntent.*
import com.eywa.projectnummi.model.HasNameAndId
import com.eywa.projectnummi.model.objects.Account
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.model.objects.Person
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.navigation.NummiNavArgument
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialogIntent
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialogState
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogIntent
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialogIntent
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialogState
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectItemDialogIntent
import com.eywa.projectnummi.utils.div100String
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTransactionsViewModel @Inject constructor(
        db: NummiDatabase,
        savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val categoryRepo = db.categoryRepo()
    private val personRepo = db.personRepo()
    private val accountRepo = db.accountRepo()
    private val transactionRepo = db.transactionRepo()

    private val _state = MutableStateFlow(
            AddTransactionsState(
                    isRecurring = savedStateHandle.get<Boolean>(NummiNavArgument.TICK_SAVE_AS_RECURRING.toArgName())
                            ?: false
            )
    )
    val state = _state.asStateFlow()

    private var getTransactionJob: Job? = null

    init {
        val createFromRecurring =
                savedStateHandle.get<Boolean>(NummiNavArgument.INIT_FROM_RECURRING_TRANSACTION.toArgName()) ?: false
        savedStateHandle.get<String>(NummiNavArgument.TRANSACTION_ID.toArgName())?.let { transactionId ->
            getTransactionJob = viewModelScope.launch {
                val transaction = transactionRepo.getFull(transactionId.toInt()).first()
                _state.update {
                    val newState = it.copy(
                            editing = Transaction(transaction),
                            creatingFromRecurring = createFromRecurring,
                    ).resetState()
                    if (!createFromRecurring) newState else newState.copy(editing = null)
                }
            }
        }
        viewModelScope.launch {
            categoryRepo.getFull().collect { categories ->
                _state.update { it.copy(categories = categories.map { dbCat -> Category(dbCat) }) }
            }
        }
        viewModelScope.launch {
            transactionRepo.getFull(true).collect { transactions ->
                _state.update { it.copy(recurringTransactions = transactions.map { dbTran -> Transaction(dbTran) }) }
            }
        }
        viewModelScope.launch {
            personRepo.get().collect { people ->
                _state.update { it.copy(people = people.map { dbPerson -> Person(dbPerson) }) }
            }
        }
        viewModelScope.launch {
            accountRepo.get().collect { accounts ->
                _state.update { it.copy(accounts = accounts.map { dbAccount -> Account(dbAccount) }) }
            }
        }
    }

    fun handle(action: AddTransactionsIntent) {
        Log.d("AddTransactionsIntent", "Handler invoked for " + action::class.simpleName)
        when (action) {
            is ValueChangedIntent -> handleValueChangedIntent(action)

            is StartChangeCategory -> _state.update {
                if (it.categories == null || it.categories.isNotEmpty()) {
                    it.copy(currentRow = action.rowIndex, selectCategoryDialogIsShown = true)
                }
                else {
                    it.copy(
                            currentRow = action.rowIndex,
                            createCategoryDialogState = CreateCategoryDialogState(categories = it.categories),
                    )
                }
            }
            is CreateCategoryDialogAction -> handleCreateCategoryIntent(action.action)
            is SelectCategoryDialogAction -> handleSelectCategoryIntent(action.action)

            is StartChangePerson -> openSelectPersonDialog(action.rowIndex, SelectPersonDialogPurpose.SELECT)
            is CreatePersonDialogAction -> handleCreatePersonIntent(action.action)
            is SelectPersonDialogAction -> handleSelectPersonIntent(action.action)

            StartChangeAccount -> _state.update {
                if (it.accounts == null || it.accounts.isNotEmpty()) {
                    it.copy(selectAccountDialogIsShown = true)
                }
                else {
                    it.copy(createAccountDialogState = CreateAccountDialogState())
                }
            }
            is CreateAccountDialogAction -> handleCreateAccountIntent(action.action)
            is SelectAccountDialogAction -> handleSelectAccountIntent(action.action)

            is SelectTransactionDialogAction -> handleSelectTransactionIntent(action.action)
            StartSelectTransaction -> _state.update { it.copy(selectTransactionDialogIsShown = true) }

            Clear -> _state.update { it.copy(creatingFromRecurring = false).resetState() }
            Submit -> submit()

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

    private fun AddTransactionsState.resetState() = AddTransactionsState(
            creatingFromRecurring = creatingFromRecurring,
            editing = editing,
            recurringTransactions = recurringTransactions,
            categories = categories,
            people = people,
            accounts = accounts,
    )

    private fun submit() = viewModelScope.launch {
        val transaction = state.value
                .takeIf { it.name.isNotBlank() }
                ?.asTransaction()
                ?.takeIf { it.amounts.isNotEmpty() }
                ?: return@launch

        if (state.value.isEditing) {
            val originalTransaction = state.value.editing!!
            transactionRepo.update(
                    transaction.asDbTransaction(),
                    transaction.getDbAmounts(originalTransaction.id),
                    originalTransaction.asDbTransaction(),
                    originalTransaction.getDbAmounts(originalTransaction.id),
            )
            _state.update { it.copy(isEditComplete = true) }
        }
        else {
            if (transaction.isRecurring) {
                transactionRepo.insert(
                        transaction.asDbTransaction(),
                        transaction.getDbAmounts(null),
                )
            }
            transactionRepo.insert(
                    transaction.copy(isRecurring = false).asDbTransaction(),
                    transaction.getDbAmounts(null),
            )
            _state.update {
                it.copy(
                    name = "",
                    note = "",
                    isOutgoing = true,
                    amountRows = listOf(AmountInputState()),
                    creatingFromRecurring = false,
                )
            }
        }
        getTransactionJob?.cancel()
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
            is NoteChanged -> _state.update { it.copy(note = action.note) }
            ToggleIsOutgoing -> _state.update { it.copy(isOutgoing = !it.isOutgoing) }
            ToggleIsRecurring -> _state.update { it.copy(isRecurring = !it.isRecurring) }
        }
    }

    private fun List<AmountInputState>.updateAt(index: Int, mutator: AmountInputState.() -> AmountInputState) =
            take(index)
                    .plus(get(index).mutator())
                    .plus(drop(index + 1))

    private fun handleCreateCategoryIntent(action: CreateCategoryDialogIntent) {
        when (action) {
            is CreateCategoryDialogIntent.LocalChange ->
                _state.update {
                    val createState = it.createCategoryDialogState ?: return
                    it.copy(createCategoryDialogState = action.updateState(createState))
                }
            CreateCategoryDialogIntent.Close -> _state.update { it.copy(createCategoryDialogState = null) }
            CreateCategoryDialogIntent.Submit -> {
                val category = _state.value.createCategoryDialogState?.asDatabaseCategory() ?: return
                viewModelScope.launch {
                    val id = categoryRepo.insert(category).toInt()
                    // Create category can only be triggered from the select category action
                    //      so after the category is created, select them
                    handleSelectCategoryIntent(
                            SelectItemDialogIntent.ItemChosen(
                                    object : HasNameAndId {
                                        override fun getItemName(): String = category.name
                                        override fun getItemId(): Int = id
                                    }
                            )
                    )
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

    private fun handleSelectCategoryIntent(action: SelectItemDialogIntent) {
        when (action) {
            SelectItemDialogIntent.Close -> _state.update { it.copy(selectCategoryDialogIsShown = false) }
            is SelectItemDialogIntent.ItemChosen<*> -> _state.update { it.setCategoryId(action.item?.getItemId()) }
            SelectItemDialogIntent.CreateNew ->
                _state.update {
                    it.copy(
                            selectCategoryDialogIsShown = false,
                            createCategoryDialogState = CreateCategoryDialogState(categories = it.categories),
                    )
                }
            else -> throw NotImplementedError()
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
                    val id = personRepo.insert(person.asDbPerson()).toInt()
                    // Create person can only be triggered from the select person action
                    //      so after the person is created, select them
                    handleSelectPersonIntent(
                            SelectItemDialogIntent.ItemChosen(person.copy(id = id))
                    )
                }
            }
        }
    }

    private fun handleSelectPersonIntent(action: SelectItemDialogIntent) {
        when (action) {
            SelectItemDialogIntent.Close -> _state.update { it.copy(selectPersonDialogIsShown = null) }
            is SelectItemDialogIntent.ItemChosen<*> ->
                _state.update {
                    if (it.selectPersonDialogIsShown == SelectPersonDialogPurpose.SELECT) {
                        it.setPersonId(action.item?.getItemId())
                    }
                    else {
                        val people = listOf(
                                it.amountRows[0].personId,
                                action.item?.getItemId()
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
            SelectItemDialogIntent.CreateNew ->
                _state.update {
                    it.copy(
                            selectPersonDialogIsShown = null,
                            createPersonDialogState = CreatePersonDialogState(),
                    )
                }
            else -> throw NotImplementedError()
        }
    }

    private fun handleCreateAccountIntent(action: CreateAccountDialogIntent) {
        when (action) {
            is CreateAccountDialogIntent.TypeChanged,
            is CreateAccountDialogIntent.NameChanged,
            ->
                _state.update {
                    val createState = it.createAccountDialogState ?: return
                    it.copy(createAccountDialogState = action.updateState(createState))
                }
            CreateAccountDialogIntent.Close -> _state.update { it.copy(createAccountDialogState = null) }
            CreateAccountDialogIntent.Submit -> {
                val account = _state.value.createAccountDialogState?.asAccount() ?: return
                viewModelScope.launch {
                    val id = accountRepo.insert(account.asDbAccount()).toInt()
                    // Create account can only be triggered from the select account action
                    //      so after the account is created, select them
                    handleSelectAccountIntent(
                            SelectItemDialogIntent.ItemChosen(account.copy(id = id))
                    )
                }
            }
        }
    }

    private fun handleSelectAccountIntent(action: SelectItemDialogIntent) {
        when (action) {
            SelectItemDialogIntent.Close -> _state.update { it.copy(selectAccountDialogIsShown = false) }
            is SelectItemDialogIntent.ItemChosen<*> ->
                _state.update {
                    it.copy(
                            accountId = action.item?.getItemId(),
                            selectAccountDialogIsShown = false,
                    )
                }
            SelectItemDialogIntent.CreateNew ->
                _state.update {
                    it.copy(
                            selectAccountDialogIsShown = false,
                            createAccountDialogState = CreateAccountDialogState(),
                    )
                }
            else -> throw NotImplementedError()
        }
    }

    private fun handleSelectTransactionIntent(action: SelectItemDialogIntent) {
        when (action) {
            SelectItemDialogIntent.Close -> _state.update { it.copy(selectTransactionDialogIsShown = false) }
            is SelectItemDialogIntent.ItemChosen<*> -> {
                if (action.item == null) return
                _state.update {
                    val date = it.date
                    it.copy(
                            creatingFromRecurring = true,
                            editing = action.item as Transaction,
                            selectTransactionDialogIsShown = false,
                    )
                            .resetState()
                            .copy(editing = null, date = date)
                }
            }
            else -> throw NotImplementedError()
        }
    }
}
