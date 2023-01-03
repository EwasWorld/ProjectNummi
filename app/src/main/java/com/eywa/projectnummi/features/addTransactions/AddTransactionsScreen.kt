package com.eywa.projectnummi.features.addTransactions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eywa.projectnummi.components.createCategoryDialog.CreateCategoryDialog
import com.eywa.projectnummi.components.createPersonDialog.CreatePersonDialog
import com.eywa.projectnummi.features.addTransactions.AddTransactionsIntent.*
import com.eywa.projectnummi.features.addTransactions.selectCategoryDialog.SelectCategoryDialog
import com.eywa.projectnummi.features.addTransactions.selectCategoryDialog.SelectCategoryDialogState
import com.eywa.projectnummi.features.addTransactions.selectPersonDialog.SelectPersonDialog
import com.eywa.projectnummi.features.addTransactions.selectPersonDialog.SelectPersonDialogState
import com.eywa.projectnummi.model.Category
import com.eywa.projectnummi.model.Person
import com.eywa.projectnummi.ui.components.*
import com.eywa.projectnummi.ui.theme.NummiTheme
import com.eywa.projectnummi.ui.theme.asClickableStyle
import com.eywa.projectnummi.ui.utils.DateTimeFormat
import java.util.*


@Composable
fun AddTransactionsScreen(
        viewModel: AddTransactionsViewModel = viewModel(),
) {
    val state = viewModel.state.collectAsState()
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
            isShown = state.selectPersonDialogIsShown,
            state = SelectPersonDialogState(state.people ?: listOf()),
            listener = { listener(SelectPersonDialogAction(it)) },
    )
    CreatePersonDialog(
            isShown = true,
            state = state.createPersonDialogState,
            listener = { listener(CreatePersonDialogAction(it)) },
    )

    Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                    .fillMaxSize()
                    .padding(NummiTheme.dimens.screenPadding)
    ) {
        DateRow(
                date = state.date,
                onIncrement = { listener(DateIncremented(it)) },
                onChange = { listener(DateChanged(it)) },
        )
        NameRow(
                name = state.name,
                onChange = { listener(NameChanged(it)) },
        )
        AmountRow(
                amount = state.amount,
                onChange = { listener(AmountChanged(it)) },
        )
        CategoryRow(
                category = state.category,
                onClick = { listener(StartChangeCategory) },
        )
        PersonRow(
                person = state.person,
                onClick = { listener(StartChangePerson) },
        )
        OutgoingRow(
                isOutgoing = state.isOutgoing,
                onClick = { listener(ToggleIsOutgoing) },
        )
        SubmitRow(
                onClick = { listener(CreateTransaction) },
        )
    }
}

@Composable
private fun DateRow(
        date: Calendar,
        onIncrement: (Int) -> Unit,
        onChange: (Calendar) -> Unit,
) {
    val context = LocalContext.current
    val datePicker by lazy {
        DatePicker.createDialog(context, date) { onChange(it) }
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
private fun NameRow(
        name: String,
        onChange: (String) -> Unit,
) {
    NummiTextField(
            text = name,
            onTextChanged = { onChange(it) },
            label = "Name",
            placeholderText = "Tesco",
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AmountRow(
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
            modifier = Modifier.padding(bottom = 10.dp)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CategoryRow(
        category: Category?,
        onClick: () -> Unit,
) {
    Surface(
            color = Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            onClick = onClick,
    ) {
        Box {
            if (category != null) {
                Box(
                        modifier = Modifier
                                .matchParentSize()
                                .clip(CornerTriangleShape(isTop = false, xScale = 2f, yScale = 2f))
                                .background(category.color)
                )
            }
            Text(
                    text = category?.name ?: "No category",
                    color = NummiTheme.colors.appBackground.content,
                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 25.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PersonRow(
        person: Person?,
        onClick: () -> Unit,
) {
    Surface(
            color = Color.Transparent,
            border = BorderStroke(NummiTheme.dimens.listItemBorder, NummiTheme.colors.listItemBorder),
            shape = NummiTheme.shapes.generalListItem,
            onClick = onClick,
    ) {
        Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 25.dp)
        ) {
            Text(
                    text = person?.name ?: "No person",
                    color = NummiTheme.colors.appBackground.content,
            )
        }
    }
}

@Composable
private fun OutgoingRow(
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
private fun SubmitRow(
        onClick: () -> Unit,
) {
    Button(
            colors = NummiTheme.colors.generalButton(),
            shape = NummiTheme.shapes.generalButton,
            onClick = onClick,
    ) {
        Text(
                text = "Create"
        )
    }
}

@Preview
@Composable
fun AddTransactionsScreen_Preview() {
    NummiScreenPreviewWrapper {
        AddTransactionsScreen(
                AddTransactionsState(
                        amount = "12.05",
                )
        ) {}
    }
}
