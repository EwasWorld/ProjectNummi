package com.eywa.projectnummi.features.viewTransactions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
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
import com.eywa.projectnummi.ui.utils.DateTimeFormat

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
    val displayItems = state.transactions.sortedByDescending { it.date }

    LazyColumn(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                    .fillMaxSize()
                    .padding(NummiTheme.dimens.screenPadding)
    ) {
        items(displayItems) { item ->
            Surface(
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, BaseColor.GREY),
                    shape = RoundedCornerShape(30),
                    modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp)
                ) {
                    Text(
                            text = DateTimeFormat.SHORT_DATE.format(item.date),
                            color = NummiTheme.colors.appBackground.content,
                    )
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                                text = item.name,
                                color = NummiTheme.colors.appBackground.content,
                        )
                        Text(
                                text = "£%.2f".format(item.amount / 100.0),
                                color = NummiTheme.colors.appBackground.content,
                        )
                    }
                }
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
