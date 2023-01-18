package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.model.providers.TransactionProvider
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun TransactionItem(
        transaction: Transaction,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
) {
    val colors = transaction.amounts.sortedBy { it.amount }.map { it.category?.color }
    Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
    ) {
        CornerTriangleBox(
                colors = colors,
                state = CornerTriangleShapeState(
                        isTop = false,
                        xScale = 2f,
                        yScale = if (colors.size > 1) 1.5f else 2f,
                ),
        )
        Text(
                text = transaction.name,
                color = NummiTheme.colors.appBackground.content,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(contentPadding)
        )
    }
}

@Preview
@Composable
fun TransactionItem_Preview() {
    NummiPreviewWrapper {
        TransactionItem(transaction = TransactionProvider.basic[3])
    }
}
