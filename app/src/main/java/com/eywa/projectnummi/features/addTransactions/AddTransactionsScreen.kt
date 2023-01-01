package com.eywa.projectnummi.features.addTransactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme


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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTransactionsScreen(
        state: AddTransactionsState,
        listener: (AddTransactionsIntent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                    .fillMaxSize()
                    .padding(NummiTheme.dimens.screenPadding)
    ) {
        OutlinedTextField(
                value = state.name,
                onValueChange = { listener(AddTransactionsIntent.NameChanged(it.stripNewLines())) },
                keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                ),
                label = {
                    Text(
                            text = "Name"
                    )
                },
                placeholder = {
                    Text(
                            text = "Tesco"
                    )
                },
                colors = NummiTheme.colors.outlinedTextField()
        )
        OutlinedTextField(
                value = state.amount,
                onValueChange = { listener(AddTransactionsIntent.AmountChanged(it.stripNewLines())) },
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
                colors = NummiTheme.colors.outlinedTextField()
        )
        Button(
                colors = NummiTheme.colors.generalButton(),
                shape = NummiTheme.shapes.generalButton,
                onClick = { listener(AddTransactionsIntent.CreateTransaction) },
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
