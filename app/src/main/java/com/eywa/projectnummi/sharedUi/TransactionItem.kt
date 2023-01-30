package com.eywa.projectnummi.sharedUi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.model.objects.Account
import com.eywa.projectnummi.model.objects.Amount
import com.eywa.projectnummi.model.objects.Transaction
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.model.providers.TransactionProvider
import com.eywa.projectnummi.sharedUi.utils.NummiIconInfo
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.utils.DateTimeFormat
import com.eywa.projectnummi.utils.asCurrency
import com.eywa.projectnummi.utils.div100String
import com.google.accompanist.flowlayout.FlowRow
import java.util.*

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
fun TransactionItemFull(
        item: Transaction,
        showCompact: Boolean,
        modifier: Modifier = Modifier,
) {
    val needsAmountsDetail = item.amounts.size > 1
            || item.amounts.first().let { it.category != null || it.person != null }

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
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxWidth()
                ) {
                    TopRowInfo(item, item.isRecurring, showCompact)
                    MainInfo(item)
                }
                AnimatedVisibility(visible = !showCompact) {
                    Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                    ) {
                        Divider(
                                color = NummiTheme.colors.divider,
                        )

                        if (!needsAmountsDetail && item.note == null) {
                            Text(
                                    text = "No additional info",
                                    color = NummiTheme.colors.appBackground.content,
                            )
                        }

                        if (needsAmountsDetail) {
                            AmountRows(item.amounts)
                        }

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
    }
}

@Composable
private fun TopRowInfo(item: Transaction, isRecurring: Boolean, isCompact: Boolean) {
    val hasAccount = item.account != null
    val icons = TransactionContent.values().filter { it.show(item) }

    if (hasAccount || !isRecurring || icons.isNotEmpty()) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
        ) {
            if (!isRecurring) {
                Text(
                        text = DateTimeFormat.SHORT_DATE.format(item.date),
                        color = NummiTheme.colors.appBackground.content,
                        overflow = TextOverflow.Ellipsis,
                )
            }

            if (icons.isNotEmpty()) {
                AnimatedVisibility(visible = isCompact) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        icons.forEach { it.Icon() }
                    }
                }
            }

            if (hasAccount) {
                Text(
                        text = item.account!!.name,
                        color = NummiTheme.colors.appBackground.content,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.End,
                        maxLines = if (isCompact) 1 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MainInfo(item: Transaction, modifier: Modifier = Modifier) {
    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                    .fillMaxWidth()
    ) {
        Text(
                text = item.name,
                color = NummiTheme.colors.appBackground.content,
                style = NummiTheme.typography.h5,
                modifier = Modifier.weight(1f)
        )
        Text(
                text = "£" + item.amounts.sumOf { it.amount }.div100String(),
                color = NummiTheme.colors.appBackground.content,
                style = NummiTheme.typography.h6,
        )
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
        val defaultColor = NummiTheme.colors.pieChartDefault
        CornerTriangleBox(
                colors = amounts.map { it.category?.displayColor ?: defaultColor }.reversed(),
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
private fun AmountRows(amounts: List<Amount>) {
    val hasMultiplePeople = amounts.distinctBy { it.person }.size > 1
    val hasMultipleCategories = amounts.distinctBy { it.category }.size > 1

    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        amounts.forEach { amount ->
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                FlowRow(
                        crossAxisSpacing = 5.dp,
                        modifier = Modifier.weight(1f)
                ) {
                    if (amount.category != null || hasMultipleCategories) {
                        Text(
                                text = amount.category?.allNames?.joinToString(" - ")
                                        ?: "No category",
                                color = NummiTheme.colors.appBackground.content,
                                modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                    if (amount.person?.id != null || hasMultiplePeople) {
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
                }
                Text(
                        text = amount.amount.div100String().asCurrency(),
                        color = NummiTheme.colors.appBackground.content,
                )
            }
        }
    }
}

private enum class TransactionContent(val info: NummiIconInfo, val show: (Transaction) -> Boolean) {
    CATEGORIES(
            NummiIconInfo.PainterIcon(
                    com.eywa.projectnummi.R.drawable.ic_category_outline,
                    contentDescription = "Has multiple categories",
            ),
            { transaction -> transaction.amounts.distinctBy { it.category }.size > 1 }
    ),
    PEOPLE(
            NummiIconInfo.VectorIcon(
                    Icons.Outlined.Person,
                    contentDescription = "Has multiple people",
            ),
            { transaction -> transaction.amounts.distinctBy { it.person }.size > 1 }
    ),
    NOTE(
            NummiIconInfo.PainterIcon(
                    com.eywa.projectnummi.R.drawable.ic_note_outline,
                    contentDescription = "Has note",
            ),
            { it.note != null }
    ),
    ;

    @Composable
    fun Icon() {
        info.copyIcon(
                tint = NummiTheme.colors.appBackground.content,
                modifier = Modifier.scale(0.7f)
        ).NummiIcon()
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
fun Compact_TransactionItem_Preview(
        @PreviewParameter(CompactPreviewParamProvider::class) param: Transaction
) {
    NummiPreviewWrapper(
            contentPadding = PaddingValues(10.dp),
    ) {
        TransactionItemFull(item = param, showCompact = true)
    }
}

@Preview
@Composable
fun CompactRecurring_TransactionItem_Preview(
        @PreviewParameter(CompactPreviewParamProvider::class) param: Transaction
) {
    NummiPreviewWrapper(
            contentPadding = PaddingValues(10.dp),
    ) {
        TransactionItemFull(item = param.copy(isRecurring = true), showCompact = true)
    }
}

@Preview
@Composable
fun Full_TransactionItem_Preview() {
    NummiPreviewWrapper(
            contentPadding = PaddingValues(10.dp),
    ) {
        TransactionItemFull(item = TransactionProvider.basic[3], true)
    }
}

// TODO Screenshot testing to enumerate possibilities
class CompactPreviewParamProvider : CollectionPreviewParameterProvider<Transaction>(
        listOf(
                Transaction(
                        id = 0,
                        date = Calendar.getInstance(),
                        name = "Amazon but with a very long name for no reason",
                        amounts = listOf(Amount(id = 0, amount = 10_00)),
                ),
                Transaction(
                        id = 0,
                        date = Calendar.getInstance(),
                        name = "Amazon",
                        amounts = listOf(
                                Amount(id = 0, amount = 10_00, category = CategoryProvider.basic[0]),
                                Amount(id = 1, amount = 10_00, category = CategoryProvider.basic[1]),
                        ),
                ),
                Transaction(
                        id = 0,
                        date = Calendar.getInstance(),
                        name = "Amazon",
                        amounts = listOf(
                                Amount(id = 0, amount = 10_00, person = PeopleProvider.basic[0]),
                                Amount(id = 1, amount = 10_00, person = PeopleProvider.basic[1]),
                        ),
                        account = Account(id = 0, name = "Here is some stupidly long account name", type = null),
                        note = "Hi, I'm a note",
                ),
                Transaction(
                        id = 0,
                        date = Calendar.getInstance(),
                        name = "Amazon",
                        amounts = listOf(
                                Amount(id = 0, amount = 10_00, category = CategoryProvider.basic[0]),
                                Amount(id = 1, amount = 10_00, person = PeopleProvider.basic[1]),
                        ),
                        account = Account(id = 0, name = "NatWest", type = null),
                        note = "Hi, I'm a note",
                ),
        )
)
