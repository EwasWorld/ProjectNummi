package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.model.NamedItem
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun <T : NamedItem> TabSwitcher(
        items: Iterable<T>,
        selectedItem: T,
        itemClickedListener: (T) -> Unit,
        modifier: Modifier = Modifier,
        paddingValues: PaddingValues = PaddingValues(start = 12.dp, end = 12.dp, top = 20.dp),
) {
    require(items.count() >= 2) { "Must have at least two items" }

    val cornerRoundPercent = 30

    Box(
            modifier = modifier
    ) {
        Surface(
                shape = RoundedCornerShape(cornerRoundPercent, cornerRoundPercent, 0, 0),
                border = BorderStroke(NummiTheme.dimens.pillBorder, NummiTheme.colors.pillSelectorBorder),
                modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(paddingValues)
        ) {
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = selectedItem == item
                    val colors = NummiTheme.colors.getPillColor(isSelected)

                    Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                    .selectable(isSelected, role = Role.Tab) { itemClickedListener(item) }
                                    .weight(1f)
                                    .background(colors.main)
                                    .fillMaxHeight()
                                    .padding(10.dp)
                    ) {
                        Text(
                                text = item.getItemName(),
                                color = colors.content,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                        )
                    }
                    if (index != items.count() - 1) {
                        Divider(
                                color = NummiTheme.colors.pillSelectorBorder,
                                modifier = Modifier
                                        .fillMaxHeight()
                                        .width(NummiTheme.dimens.pillBorder)
                        )
                    }
                }
            }
        }

        Divider(
                color = NummiTheme.colors.pillSelectorBorder,
                modifier = Modifier
                        .fillMaxWidth()
                        .width(NummiTheme.dimens.pillBorder)
                        .align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TabSwitcher_Preview() {
    NummiTheme {
        TabSwitcher(
                items = ManageTabSwitcherItem.values().toList(),
                selectedItem = ManageTabSwitcherItem.ACCOUNTS,
                itemClickedListener = {},
        )
    }
}
