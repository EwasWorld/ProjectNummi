package com.eywa.projectnummi.features.addTransactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eywa.projectnummi.R
import com.eywa.projectnummi.features.addTransactions.AddTransactionsIntent.*
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.sharedUi.*
import com.eywa.projectnummi.sharedUi.account.AccountItem
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialog
import com.eywa.projectnummi.sharedUi.category.CategoryItem
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialog
import com.eywa.projectnummi.sharedUi.person.PersonItem
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialog
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectAccountDialog
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectCategoryDialog
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectItemDialogState
import com.eywa.projectnummi.sharedUi.selectItemDialog.SelectPersonDialog
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.utils.asCurrency
import com.eywa.projectnummi.utils.div100String
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun AddTransactionsScreen(
        navController: NavController,
        viewModel: AddTransactionsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value.isEditComplete) {
        launch {
            if (state.value.isEditComplete) {
                navController.popBackStack()
            }
        }
    }

    AddTransactionsScreen(
            state = state.value,
            listener = { viewModel.handle(it) },
    )
}

@Composable
fun AddTransactionsScreen(
        state: AddTransactionsState,
        listener: (AddTransactionsIntent) -> Unit,
) {
    Dialogs(state, listener)

    Column(
            verticalArrangement = Arrangement.spacedBy(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(NummiTheme.dimens.screenPadding)
    ) {
        MainInfo(state, listener)

        Text(
                text = "Total: " + state.totalAmount.div100String().asCurrency(),
                color = NummiTheme.colors.appBackground.content,
                style = NummiTheme.typography.h5,
        )

        repeat(state.amountRows.size) { index ->
            AmountRow(index, state, listener)
        }

        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(35.dp),
        ) {
            if (state.amountRows.size <= 1) {
                SplitButton { listener(Split) }
            }
            AddRowButton { listener(AddAmountRow) }
        }

        FinalButtons(state, listener)
    }
}

@Composable
private fun Dialogs(
        state: AddTransactionsState,
        listener: (AddTransactionsIntent) -> Unit,
) {
    SelectCategoryDialog(
            isShown = state.selectCategoryDialogIsShown,
            state = SelectItemDialogState(state.categories ?: listOf()),
            listener = { listener(SelectCategoryDialogAction(it)) },
    )
    CreateCategoryDialog(
            isShown = true,
            state = state.createCategoryDialogState,
            listener = { listener(CreateCategoryDialogAction(it)) },
    )
    SelectPersonDialog(
            isShown = state.selectPersonDialogIsShown != null,
            state = SelectItemDialogState(state.people ?: listOf()),
            listener = { listener(SelectPersonDialogAction(it)) },
    )
    CreatePersonDialog(
            isShown = true,
            state = state.createPersonDialogState,
            listener = { listener(CreatePersonDialogAction(it)) },
    )
    SelectAccountDialog(
            isShown = state.selectAccountDialogIsShown,
            state = SelectItemDialogState(state.accounts ?: listOf()),
            listener = { listener(SelectAccountDialogAction(it)) },
    )
    CreateAccountDialog(
            isShown = true,
            state = state.createAccountDialogState,
            listener = { listener(CreateAccountDialogAction(it)) },
    )
}

@Composable
private fun AmountRow(
        rowIndex: Int,
        state: AddTransactionsState,
        listener: (AddTransactionsIntent) -> Unit,
) {
    val rowState = state.amountRows[rowIndex]

    Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AmountInput(
                    amount = rowState.amount,
                    onChange = { listener(AmountChanged(rowIndex, it)) },
            )
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                BorderedItem(
                        onClick = { listener(StartChangeCategory(rowIndex)) },
                        content = { CategoryItem(category = state.getCategory(rowState.categoryId)) },
                )
                BorderedItem(
                        onClick = { listener(StartChangePerson(rowIndex)) },
                        content = { PersonItem(person = state.getPerson(rowState.personId)) },
                )
            }
        }
        if (state.amountRows.size > 1) {
            IconButton(onClick = { listener(DeleteAmountRow(rowIndex)) }) {
                Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove row",
                        tint = NummiTheme.colors.appBackground.content,
                )
            }
        }
    }
}

@Composable
private fun MainInfo(
        state: AddTransactionsState,
        listener: (AddTransactionsIntent) -> Unit,
) {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
        ) {
            DateInput(
                    date = state.date,
                    onIncrement = { listener(DateIncremented(it)) },
                    onChange = { listener(DateChanged(it)) },
            )
            CheckboxInput(
                    text = "Outgoing",
                    isSelected = state.isOutgoing,
                    onClick = { listener(ToggleIsOutgoing) },
            )
        }
        BorderedItem(
                onClick = { listener(StartChangeAccount) },
                content = { AccountItem(account = state.account) }
        )
        NameInput(
                name = state.name,
                onChange = { listener(NameChanged(it)) },
                modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Composable
private fun DateInput(
        date: Calendar,
        onIncrement: (Int) -> Unit,
        onChange: (Calendar) -> Unit,
) {
    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        IconButton(onClick = { onIncrement(-1) }) {
            Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "One day earlier",
                    tint = NummiTheme.colors.appBackground.content,
            )
        }
        DateText(
                style = NummiTheme.typography.h6,
                date = date,
                onChange = onChange,
        )
        IconButton(onClick = { onIncrement(1) }) {
            Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "One day later",
                    tint = NummiTheme.colors.appBackground.content,
            )
        }
    }
}

@Composable
private fun NameInput(
        name: String,
        modifier: Modifier = Modifier,
        onChange: (String) -> Unit,
) {
    NummiTextField(
            text = name,
            onTextChanged = { onChange(it) },
            label = "Name",
            placeholderText = "Tesco",
            modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AmountInput(
        amount: String,
        onChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
            value = amount,
            onValueChange = { onChange(it.stripNewLines()) },
            keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            label = {
                Text(
                        text = "Amount"
                )
            },
            placeholder = {
                Text(
                        text = "10.99"
                )
            },
            colors = NummiTheme.colors.outlinedTextField(),
    )
}

@Composable
private fun FinalButtons(
        state: AddTransactionsState,
        listener: (AddTransactionsIntent) -> Unit,
) {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
    ) {
        if (!state.isEditing && !state.creatingFromRecurring) {
            CheckboxInput(
                    text = "Save to frequently used",
                    isSelected = state.isRecurring,
                    onClick = { listener(ToggleIsRecurring) },
                    modifier = Modifier.padding(start = 15.dp)
            )
        }
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(
                    colors = NummiTheme.colors.secondaryTextButton(),
                    elevation = NummiTheme.dimens.buttonElevationNone(),
                    onClick = { listener(Clear) },
            ) {
                Text(
                        text = if (state.isEditing) "Reset" else "Clear"
                )
            }
            Button(
                    colors = NummiTheme.colors.generalButton(),
                    shape = NummiTheme.shapes.generalButton,
                    onClick = { listener(Submit) },
            ) {
                Text(
                        text = if (state.isEditing) "Update" else "Create"
                )
            }
        }
    }
}

@Composable
private fun SplitButton(
        onClick: () -> Unit,
) {
    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
                painter = painterResource(R.drawable.ic_tenancy_outline_300),
                contentDescription = "Split",
                tint = NummiTheme.colors.appBackground.content,
        )
        Text(
                text = "Split",
                color = NummiTheme.colors.appBackground.content,
        )
    }
}

@Composable
private fun AddRowButton(
        onClick: () -> Unit,
) {
    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                    .clickable(onClick = onClick)
                    .padding(end = 5.dp)
    ) {
        Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add row",
                tint = NummiTheme.colors.appBackground.content,
        )
        Text(
                text = "Add row",
                color = NummiTheme.colors.appBackground.content,
        )
    }
}

@Preview
@Composable
fun AddTransactionsScreen_Preview() {
    NummiScreenPreviewWrapper {
        AddTransactionsScreen(
                AddTransactionsState(
                        categories = CategoryProvider.basic,
                        amountRows = listOf(
                                AmountInputState(
                                        amount = "12.05",
                                        categoryId = 1,
                                        personId = 0,
                                ),
                                AmountInputState(
                                        amount = "",
                                        categoryId = 0,
                                        personId = 1,
                                ),
                        ),
                        people = PeopleProvider.basic,
                        accounts = AccountProvider.basic,
                        accountId = 1,
                )
        ) {}
    }
}
