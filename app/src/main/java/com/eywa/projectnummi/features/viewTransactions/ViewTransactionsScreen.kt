package com.eywa.projectnummi.features.viewTransactions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eywa.projectnummi.model.providers.TransactionProvider
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme
import com.eywa.projectnummi.ui.theme.colors.BaseColor

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
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                    .fillMaxSize()
                    .padding(NummiTheme.dimens.screenPadding)
    ) {
        items(state.transactions) { item ->
            Surface(
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, BaseColor.GREY),
                    shape = RoundedCornerShape(100),
                    modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                        text = "£%.2f".format(item.amount / 100.0),
                        color = NummiTheme.colors.appBackground.content,
                        modifier = Modifier.padding(10.dp)
                )
            }
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
