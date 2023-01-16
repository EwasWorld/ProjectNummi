package com.eywa.projectnummi.sharedUi.person

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eywa.projectnummi.model.objects.Person
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun PersonItem(
        person: Person?,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
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

@Composable
fun PersonItem(
        people: List<Person?>,
        contentPadding: PaddingValues = PaddingValues(NummiTheme.dimens.listItemPadding),
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
