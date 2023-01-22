package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.model.providers.TransactionProvider
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.utils.div100String

@Composable
fun TransactionItem(
        transaction: Transaction,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
) {
    val colors = transaction.amounts.sortedBy { it.amount }.map { it.category?.displayColor }
    Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
    ) {
        CornerTriangleBox(
                color = NummiTheme.colors.getTransactionColor(transaction.isOutgoing),
                state = CornerTriangleShapeState(
                        isTop = false,
                        isLeft = false,
                        xScale = 0.8f,
                        yScale = 0.8f,
                ),
                modifier = Modifier.alpha(0.3f)
        )
        CornerTriangleBox(
                colors = colors,
                state = CornerTriangleShapeState(
                        isTop = false,
                        xScale = 2f,
                        yScale = if (colors.size > 1) 1.5f else 2f,
                ),
        )
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(contentPadding)
        ) {
            Text(
                    text = transaction.name,
                    color = NummiTheme.colors.appBackground.content,
                    modifier = Modifier.weight(1f)
            )
            Text(
                    text = "£" + transaction.amounts.sumOf { it.amount }.div100String(),
                    color = NummiTheme.colors.appBackground.content,
            )
        }
    }
}

@Preview
@Composable
fun TransactionItem_Preview() {
    NummiPreviewWrapper {
        TransactionItem(transaction = TransactionProvider.basic[3])
    }
}
