package com.eywa.projectnummi.sharedUi.person

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eywa.projectnummi.model.Person
import com.eywa.projectnummi.sharedUi.BorderedItem
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun PersonItem(
        person: Person?,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
        onClick: () -> Unit,
) {
    BorderedItem(
            onClick = onClick,
            modifier = modifier
    ) {
        Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(contentPadding)
        ) {
            Text(
                    text = person?.name ?: "Me",
                    color = NummiTheme.colors.appBackground.content,
            )
        }
    }
}

@Composable
fun PersonItem(
        people: List<Person?>,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
        onClick: () -> Unit,
) {
    BorderedItem(
            onClick = onClick,
            modifier = modifier
    ) {
        Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(contentPadding)
        ) {
            Text(
                    text = when (people.size) {
                        0 -> "All people"
                        1 -> people.first()?.name ?: "Me"
                        else -> "Various people"
                    },
                    color = NummiTheme.colors.appBackground.content,
            )
        }
    }
}
