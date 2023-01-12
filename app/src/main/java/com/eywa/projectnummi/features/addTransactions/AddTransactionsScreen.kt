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
import androidx.compose.ui.platform.LocalContext
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
import com.eywa.projectnummi.sharedUi.NummiDatePicker
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.sharedUi.NummiTextField
import com.eywa.projectnummi.sharedUi.account.AccountItem
import com.eywa.projectnummi.sharedUi.account.createAccountDialog.CreateAccountDialog
import com.eywa.projectnummi.sharedUi.account.selectAccountDialog.SelectAccountDialog
import com.eywa.projectnummi.sharedUi.account.selectAccountDialog.SelectAccountDialogState
import com.eywa.projectnummi.sharedUi.category.CategoryItem
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialog
import com.eywa.projectnummi.sharedUi.category.selectCategoryDialog.SelectCategoryDialog
import com.eywa.projectnummi.sharedUi.category.selectCategoryDialog.SelectCategoryDialogState
import com.eywa.projectnummi.sharedUi.person.PersonItem
import com.eywa.projectnummi.sharedUi.person.createPersonDialog.CreatePersonDialog
import com.eywa.projectnummi.sharedUi.person.selectPersonDialog.SelectPersonDialog
import com.eywa.projectnummi.sharedUi.person.selectPersonDialog.SelectPersonDialogState
import com.eywa.projectnummi.sharedUi.stripNewLines
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.theme.asClickableStyle
import com.eywa.projectnummi.utils.DateTimeFormat
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

        FinalButtons(state.isEditing, listener)
    }
}

@Composable
private fun Dialogs(
        state: AddTransactionsState,
        listener: (AddTransactionsIntent) -> Unit,
) {
    SelectCategoryDialog(
            isShown = state.selectCategoryDialogIsShown,
            state = SelectCategoryDialogState(state.categories ?: listOf()),
            listener = { listener(SelectCategoryDialogAction(it)) },
    )
    CreateCategoryDialog(
            isShown = true,
            state = state.createCategoryDialogState,
            listener = { listener(CreateCategoryDialogAction(it)) },
    )
    SelectPersonDialog(
            isShown = state.selectPersonDialogIsShown != null,
            state = SelectPersonDialogState(state.people ?: listOf()),
            listener = { listener(SelectPersonDialogAction(it)) },
    )
    CreatePersonDialog(
            isShown = true,
            state = state.createPersonDialogState,
            listener = { listener(CreatePersonDialogAction(it)) },
    )
    SelectAccountDialog(
            isShown = state.selectAccountDialogIsShown,
            state = SelectAccountDialogState(state.accounts ?: listOf()),
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
                CategoryItem(
                        category = state.getCategory(rowState.categoryId),
                        onClick = { listener(StartChangeCategory(rowIndex)) },
                )
                PersonItem(
                        person = state.getPerson(rowState.categoryId),
                        onClick = { listener(StartChangePerson(rowIndex)) },
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
            OutgoingInput(
                    isOutgoing = state.isOutgoing,
                    onClick = { listener(ToggleIsOutgoing) },
            )
        }
        AccountItem(
                account = state.account,
                onClick = { listener(StartChangeAccount) },
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
    val context = LocalContext.current
    val dialogTheme = NummiTheme.colors.dialogThemeId
    val datePicker by lazy {
        NummiDatePicker.createDialog(context, date, dialogTheme) { onChange(it) }
    }

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
        Text(
                text = DateTimeFormat.LONG_DATE.format(date),
                style = NummiTheme.typography.h6.asClickableStyle(),
                modifier = Modifier.clickable { datePicker.show() }
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
private fun OutgoingInput(
        isOutgoing: Boolean,
        onClick: () -> Unit,
) {
    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
                text = "Outgoing",
                color = NummiTheme.colors.appBackground.content,
        )
        Checkbox(
                checked = isOutgoing,
                onCheckedChange = { onClick() },
                colors = NummiTheme.colors.generalCheckbox(),
        )
    }
}

@Composable
private fun FinalButtons(
        isEditing: Boolean,
        listener: (AddTransactionsIntent) -> Unit,
) {
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
                    text = if (isEditing) "Reset" else "Clear"
            )
        }
        Button(
                colors = NummiTheme.colors.generalButton(),
                shape = NummiTheme.shapes.generalButton,
                onClick = { listener(Submit) },
        ) {
            Text(
                    text = if (isEditing) "Update" else "Create"
            )
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
                                        amount = "12.05",
                                        categoryId = 0,
                                        personId = 1,
                                ),
                                AmountInputState(personId = 0)
                        ),
                        people = PeopleProvider.basic,
                        accounts = AccountProvider.basic,
                        accountId = 1,
                )
        ) {}
    }
}
