package com.eywa.projectnummi.features.addTransactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper


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

@Composable
fun AddTransactionsScreen(
        state: AddTransactionsState,
        listener: (AddTransactionsIntent) -> Unit,
) {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
    ) {
        TextField(
                value = state.enteredAmount,
                onValueChange = { listener(AddTransactionsIntent.AmountChanged(it)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                        onDone = {
                            listener(AddTransactionsIntent.CreateTransaction)
                        },
                )
        )
    }
}

@Preview
@Composable
fun AddTransactionsScreen_Preview() {
    NummiScreenPreviewWrapper {
        AddTransactionsScreen(
                AddTransactionsState(
                        enteredAmount = "12.05",
                )
        ) {}
    }
}
