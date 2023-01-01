package com.eywa.projectnummi.features.viewTransactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eywa.projectnummi.model.providers.TransactionProvider
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper

@Composable
fun ViewTransactionsScreen(
        viewModel: ViewTransactionsViewModel = viewModel()
) {
    val state = viewModel.state.collectAsState()
    ViewTransactionsScreen(
            state = state.value,
    )
}


@Composable
fun ViewTransactionsScreen(
        state: ViewTransactionsState,
) {
    LazyColumn(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxSize()
    ) {
        items(state.transactions) { item ->
            Text(
                    text = "£%.2f".format(item.amount / 100.0),
            )
        }
    }
}

@Preview
@Composable
fun ViewTransactionsScreen_Preview() {
    NummiScreenPreviewWrapper {
        ViewTransactionsScreen(
                ViewTransactionsState(
                        transactions = TransactionProvider.basic,
                )
        )
    }
}
