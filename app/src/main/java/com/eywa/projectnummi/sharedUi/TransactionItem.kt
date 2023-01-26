package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.model.objects.Amount
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.model.providers.TransactionProvider
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.utils.DateTimeFormat
import com.eywa.projectnummi.utils.asCurrency
import com.eywa.projectnummi.utils.div100String

@Composable
fun TransactionItemTiny(
        transaction: Transaction,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
) {
    val colors = transaction.amounts.sortedBy { it.amount }.map { it.category?.displayColor }
    Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
    ) {
        CornerTriangles(
                isOutgoing = transaction.isOutgoing,
                amounts = transaction.amounts,
                categoryXScale = 2f,
                categoryYScale = if (colors.size > 1) 1.5f else 2f,
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

@Composable
fun TransactionItemCompact(
        item: Transaction,
        modifier: Modifier = Modifier,
        isRecurring: Boolean = item.isRecurring,
) {
    BorderedItem(
            modifier = modifier.fillMaxWidth()
    ) {
        Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
        ) {
            CornerTriangles(
                    isOutgoing = item.isOutgoing,
                    amounts = item.amounts,
                    forceSize = with(LocalDensity.current) { NummiTheme.dimens.viewTransactionTriangleSize.toPx() },
                    categoryYScale = 1.5f,
            )
            Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                MainInfo(item = item, isRecurring = isRecurring)
                // TODO TransactionItemCompact
            }
        }
    }
}

@Composable
fun TransactionItemFull(
        item: Transaction,
        modifier: Modifier = Modifier,
        isRecurring: Boolean = item.isRecurring,
) {
    BorderedItem(
            modifier = modifier.fillMaxWidth()
    ) {
        Box(
                contentAlignment = Alignment.Center,
        ) {
            CornerTriangles(
                    isOutgoing = item.isOutgoing,
                    amounts = item.amounts,
                    forceSize = with(LocalDensity.current) { NummiTheme.dimens.viewTransactionTriangleSize.toPx() },
                    categoryYScale = 1.5f,
            )

            Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                MainInfo(item, isRecurring)

                AmountRows(item.amounts)

                if (item.note != null) {
                    Text(
                            text = item.note,
                            color = NummiTheme.colors.appBackground.content,
                            modifier = Modifier.align(Alignment.Start)
                    )
                }
            }
        }
    }
}

@Composable
private fun MainInfo(item: Transaction, isRecurring: Boolean) {
    if (item.account != null) {
        Text(
                text = item.account.name,
                color = NummiTheme.colors.appBackground.content,
                fontStyle = FontStyle.Italic,
        )
    }
    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
    ) {
        Text(
                text = item.name,
                color = NummiTheme.colors.appBackground.content,
                style = NummiTheme.typography.h5,
                modifier = Modifier.weight(1f)
        )
        if (!isRecurring) {
            Text(
                    text = DateTimeFormat.SHORT_DATE.format(item.date),
                    color = NummiTheme.colors.appBackground.content,
                    modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
private fun BoxScope.CornerTriangles(
        isOutgoing: Boolean,
        amounts: List<Amount>,
        forceSize: Float? = null,
        categoryXScale: Float = 1f,
        categoryYScale: Float = 1f,
) {
    CornerTriangleBox(
            color = NummiTheme.colors.getTransactionColor(isOutgoing),
            state = CornerTriangleShapeState(
                    isTop = false,
                    isLeft = false,
                    forceSize = forceSize,
                    xScale = 0.8f,
                    yScale = 0.8f,
            ),
            modifier = Modifier.alpha(0.3f)
    )

    if (amounts.any { it.category != null }) {
        CornerTriangleBox(
                colors = amounts.map { it.category?.displayColor }.reversed(),
                state = CornerTriangleShapeState(
                        isTop = false,
                        segmentWeights = amounts.map { it.amount }.reversed(),
                        forceSize = forceSize,
                        xScale = categoryXScale,
                        yScale = categoryYScale,
                ),
        )
    }
}

@Composable
private fun ColumnScope.AmountRows(amounts: List<Amount>) {
    val uniquePeople = amounts.distinctBy { it.person }.size
    val uniqueCategories = amounts.distinctBy { it.category }.size

    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .padding(top = 5.dp)
    ) {
        amounts.forEach { amount ->
            Row(
                    verticalAlignment = Alignment.CenterVertically,
            ) {
                if (amount.category != null || uniqueCategories > 1) {
                    Text(
                            text = amount.category?.allNames?.joinToString(" - ")
                                    ?: "No category",
                            color = NummiTheme.colors.appBackground.content,
                            modifier = Modifier.padding(end = 10.dp)
                    )
                }
                if (amount.person?.id != null || uniquePeople > 1) {
                    Text(
                            text = amount.person?.name ?: "Me",
                            color = NummiTheme.colors.appBackground.content,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier
                                    .background(
                                            NummiTheme.colors.transactionAmountDetail,
                                            RoundedCornerShape(100),
                                    )
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                        text = amount.amount.div100String().asCurrency(),
                        color = NummiTheme.colors.appBackground.content,
                )
            }
        }
    }
    if (amounts.size > 1) {
        AmountsTotal(amounts)
    }
}

@Composable
private fun ColumnScope.AmountsTotal(amounts: List<Amount>) {
    val oneDpWidth = with(LocalDensity.current) { 3.dp.toPx() }
    val totalLinesColor = NummiTheme.colors.transactionTotalLines
    Box(
            modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 6.dp, bottom = 5.dp)
    ) {
        Text(
                text = amounts.sumOf { it.amount }.div100String().asCurrency(),
                color = NummiTheme.colors.appBackground.content,
                modifier = Modifier
                        .padding(bottom = 4.dp)
                        .padding(horizontal = 8.dp)
        )
        Canvas(
                modifier = Modifier.matchParentSize()
        ) {
            fun customLine(height: Float) = drawLine(
                    totalLinesColor,
                    Offset(0f, height),
                    Offset(size.width, height),
                    strokeWidth = oneDpWidth / 2f
            )

            customLine(0f)
            customLine(size.height)
            customLine(size.height - oneDpWidth * 1)
        }
    }
}

@Preview
@Composable
fun Tiny_TransactionItem_Preview() {
    NummiPreviewWrapper {
        TransactionItemTiny(transaction = TransactionProvider.basic[3])
    }
}

@Preview
@Composable
fun Compact_TransactionItem_Preview() {
    NummiPreviewWrapper {
        TransactionItemCompact(item = TransactionProvider.basic[3])
    }
}

@Preview
@Composable
fun Full_TransactionItem_Preview() {
    NummiPreviewWrapper(
            contentPadding = PaddingValues(10.dp),
    ) {
        TransactionItemFull(item = TransactionProvider.basic[3])
    }
}
