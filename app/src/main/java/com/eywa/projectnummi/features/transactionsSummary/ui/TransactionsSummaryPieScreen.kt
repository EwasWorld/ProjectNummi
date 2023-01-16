package com.eywa.projectnummi.features.transactionsSummary.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.features.transactionsSummary.TransactionsSummaryIntent
import com.eywa.projectnummi.features.transactionsSummary.TransactionsSummaryIntent.SummaryItemSelected
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryGrouping
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryPieItem
import com.eywa.projectnummi.features.transactionsSummary.state.TransactionsSummaryState
import com.eywa.projectnummi.features.transactionsSummary.state.getTotal
import com.eywa.projectnummi.model.providers.TransactionProvider
import com.eywa.projectnummi.sharedUi.BorderedItem
import com.eywa.projectnummi.sharedUi.CornerTriangleBox
import com.eywa.projectnummi.sharedUi.CornerTriangleShapeState
import com.eywa.projectnummi.sharedUi.NummiScreenPreviewWrapper
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.utils.Cartesian
import com.eywa.projectnummi.utils.Polar
import com.eywa.projectnummi.utils.div100String

@Composable
fun TransactionsSummaryPieScreen(
        state: TransactionsSummaryState,
        listener: (TransactionsSummaryIntent) -> Unit,
) {
    Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 10.dp)
    ) {
        PieWithText(
                selectedItem = state.selectedItem,
                grouping = state.currentGrouping,
                items = state.groupedItems,
                onClick = { listener(TransactionsSummaryIntent.SummaryPieClicked(it)) },
                modifier = Modifier.padding(bottom = 20.dp)
        )
        ListBreakdown(state, listener)
    }
}

@Composable
private fun ListBreakdown(
        state: TransactionsSummaryState,
        listener: (TransactionsSummaryIntent) -> Unit,
) {
    state.groupedItems.forEachIndexed { index, item ->
        BorderedItem(
                onClick = { listener(SummaryItemSelected(index)) },
        ) {
            Box(
                    contentAlignment = Alignment.Center,
            ) {
                CornerTriangleBox(
                        color = item.color ?: NummiTheme.colors.pieChartDefault,
                        state = CornerTriangleShapeState(
                                isTop = false,
                                xScale = 2f,
                                yScale = 2f,
                        ),
                )
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(NummiTheme.dimens.listItemPadding)
                ) {
                    Text(
                            text = item.name.getDisplayName(state.currentGrouping),
                            color = NummiTheme.colors.appBackground.content,
                    )
                    Text(
                            text = "£" + item.amount.div100String(),
                            color = NummiTheme.colors.appBackground.content,
                    )
                }
            }
        }
    }
}

private fun String?.getDisplayName(grouping: TransactionsSummaryGrouping) = when (this) {
    null -> when (grouping) {
        TransactionsSummaryGrouping.CATEGORY -> "No category"
        TransactionsSummaryGrouping.PERSON -> "Me"
        TransactionsSummaryGrouping.ACCOUNT -> "Default account"
        else -> throw NotImplementedError()
    }
    TransactionsSummaryGrouping.INCOMING_NAME -> "Incoming"
    TransactionsSummaryGrouping.OUTGOING_NAME -> "Outgoing"
    else -> this
}

@Composable
private fun PieCircles(
        selectedItem: TransactionsSummaryPieItem?,
        items: List<TransactionsSummaryPieItem>,
        onClick: (location: Polar) -> Unit,
) {
    val defaultColor = NummiTheme.colors.pieChartDefault
    val rotationDegrees = 270f

    Canvas(
            modifier = Modifier
                    .fillMaxSize()
                    // The clear oval doesn't work unless this is <1 - can't remember why, maybe the graphics layer?
                    .alpha(0.998f)
                    .pointerInput(items) {
                        detectTapGestures {
                            // Relative to the centre of the PieCircle
                            val clickLocation = Cartesian(it.x - size.width / 2f, it.y - size.height / 2f).toPolar()
                            onClick(clickLocation
                                    .addDegrees(-rotationDegrees))
                        }
                    }
    ) {
        if (items.isNotEmpty()) {
            items.forEach { item ->
                if (item.startAngleDegrees != null && item.arcAngleDegrees != null) {
                    drawArc(
                            color = item.color ?: defaultColor,
                            startAngle = rotationDegrees + item.startAngleDegrees,
                            sweepAngle = item.arcAngleDegrees,
                            useCenter = true,
                    )
                }
            }
        }
        else {
            drawArc(
                    color = defaultColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = true,
            )
        }

        drawCircle(
                color = Color.Transparent,
                blendMode = BlendMode.Clear,
                radius = size.minDimension * 0.35f
        )
        if (selectedItem != null) {
            drawCircle(
                    color = selectedItem.color ?: defaultColor,
                    radius = size.minDimension * 0.3f
            )
        }
    }
}

@Composable
private fun PieWithText(
        selectedItem: TransactionsSummaryPieItem?,
        grouping: TransactionsSummaryGrouping,
        items: List<TransactionsSummaryPieItem>,
        modifier: Modifier = Modifier,
        onClick: (location: Polar) -> Unit,
) {
    val pieTotal = items.getTotal() ?: 0
    val actualTotal = items.sumOf { it.amount }
    Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
    ) {
        PieCircles(selectedItem, items, onClick)

        Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                    text = selectedItem?.let { it.name.getDisplayName(grouping) } ?: "Total",
                    color = NummiTheme.colors.appBackground.content,
                    style = NummiTheme.typography.h3,
            )
            Text(
                    text = "£" + (selectedItem?.amount ?: pieTotal).div100String(),
                    color = NummiTheme.colors.appBackground.content,
                    style = NummiTheme.typography.h3,
            )
            if (selectedItem == null && actualTotal != pieTotal) {
                Text(
                        text = "£" + actualTotal.div100String(),
                        color = NummiTheme.colors.appBackground.content,
                )
            }
        }
    }
}

@Preview
@Composable
fun TransactionsSummaryPieScreen_Preview() {
    NummiScreenPreviewWrapper {
        TransactionsSummaryPieScreen(
                TransactionsSummaryState(
                        transactions = TransactionProvider.basic,
                ),
        ) {}
    }
}

@Preview
@Composable
fun Empty_TransactionsSummaryPieScreen_Preview() {
    NummiScreenPreviewWrapper {
        TransactionsSummaryPieScreen(
                TransactionsSummaryState(
                        selectedItemIndex = 0,
                ),
        ) {}
    }
}

@Preview
@Composable
fun Selected_TransactionsSummaryPieScreen_Preview() {
    NummiScreenPreviewWrapper {
        TransactionsSummaryPieScreen(
                TransactionsSummaryState(
                        transactions = TransactionProvider.basic,
                        selectedItemIndex = 0,
                ),
        ) {}
    }
}
