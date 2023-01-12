package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun <T> ItemList(
        items: List<T>?,
        hasDefaultItem: Boolean = false,
        newItemButtonText: String? = null,
        onNewItemClicked: (() -> Unit)? = null,
        contentPadding: PaddingValues = PaddingValues(0.dp),
        itemContent: @Composable (T?) -> Unit,
) {
    LazyColumn(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(NummiTheme.dimens.listItemSpacedBy),
            contentPadding = contentPadding,
    ) {
        if (hasDefaultItem) {
            item {
                itemContent(null)
            }
        }

        items(items ?: listOf()) { item ->
            itemContent(item)
        }

        if (newItemButtonText != null) {
            item {
                Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .clickable { onNewItemClicked!!.invoke() }
                ) {
                    Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = NummiTheme.colors.appBackground.content,
                    )
                    Text(
                            text = newItemButtonText,
                            color = NummiTheme.colors.appBackground.content,
                    )
                }
            }
        }
    }
}
