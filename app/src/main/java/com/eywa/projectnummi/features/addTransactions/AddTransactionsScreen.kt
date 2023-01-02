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
import com.eywa.projectnummi.features.addTransactions.AddTransactionsIntent.*
import com.eywa.projectnummi.features.addTransactions.selectCategoryDialog.SelectCategoryDialog
import com.eywa.projectnummi.features.addTransactions.selectCategoryDialog.SelectCategoryDialogState
import com.eywa.projectnummi.ui.components.CornerTriangleShape
import com.eywa.projectnummi.ui.components.DatePicker
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.components.NummiTextField
import com.eywa.projectnummi.ui.theme.NummiTheme
import com.eywa.projectnummi.ui.theme.asClickableStyle
import com.eywa.projectnummi.ui.theme.colors.BaseColor
import com.eywa.projectnummi.ui.utils.DateTimeFormat


@Composable
fun AddTransactionsScreen(
        viewModel: AddTransactionsViewModel = viewModel()
) {
    val state = viewModel.state.collectAsState()
    AddTransactionsScreen(
            state = state.value,
            listener = { viewModel.handle(it) },
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun AddTransactionsScreen(
        state: AddTransactionsState,
        listener: (AddTransactionsIntent) -> Unit,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val datePicker by lazy {
        DatePicker.createDialog(context, state.date) { listener(DateChanged(it)) }
    }

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

    Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                    .fillMaxSize()
                    .padding(NummiTheme.dimens.screenPadding)
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            IconButton(onClick = { listener(DateIncremented(-1)) }) {
                Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "One day earlier",
                        tint = NummiTheme.colors.appBackground.content,
                )
            }
            Text(
                    text = DateTimeFormat.LONG_DATE.format(state.date),
                    style = NummiTheme.typography.h6.asClickableStyle(),
                    modifier = Modifier.clickable { datePicker.show() }
            )
            IconButton(onClick = { listener(DateIncremented(1)) }) {
                Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "One day later",
                        tint = NummiTheme.colors.appBackground.content,
                )
            }
        }
        NummiTextField(
                text = state.name,
                onTextChanged = { listener(NameChanged(it)) },
                label = "Name",
                placeholderText = "Tesco",
        )
        OutlinedTextField(
                value = state.amount,
                onValueChange = { listener(AmountChanged(it.stripNewLines())) },
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
        Box {
            Surface(
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, BaseColor.GREY_500),
                    shape = NummiTheme.shapes.generalListItem,
                    onClick = { listener(StartChangeCategory) }
            ) {
                if (state.category != null) {
                    Box(
                            modifier = Modifier
                                    .matchParentSize()
                                    .clip(CornerTriangleShape(isTop = false, xScale = 2f, yScale = 2f))
                                    .background(state.category.color)
                    )
                }
                Text(
                        text = state.category?.name ?: "No category",
                        color = NummiTheme.colors.appBackground.content,
                        modifier = Modifier.padding(vertical = 15.dp, horizontal = 25.dp)
                )
            }
        }
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.clickable { listener(ToggleIsOutgoing) }
        ) {
            Text(
                    text = "Outgoing",
                    color = NummiTheme.colors.appBackground.content,
            )
            Checkbox(
                    checked = state.isOutgoing,
                    onCheckedChange = { listener(ToggleIsOutgoing) },
                    colors = NummiTheme.colors.generalCheckbox(),
            )
        }
        Button(
                colors = NummiTheme.colors.generalButton(),
                shape = NummiTheme.shapes.generalButton,
                onClick = { listener(CreateTransaction) },
        ) {
            Text(
                    text = "Create"
            )
        }
    }
}

fun String.stripNewLines() = replace("\n", "")

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
