package com.eywa.projectnummi.features.managePeople

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.eywa.projectnummi.components.person.PersonItem
import com.eywa.projectnummi.components.person.createPersonDialog.CreatePersonDialog
import com.eywa.projectnummi.features.managePeople.ManagePeopleIntent.AddPersonClicked
import com.eywa.projectnummi.features.managePeople.ManagePeopleIntent.CreatePersonDialogAction
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.ui.components.NummiScreenPreviewWrapper
import com.eywa.projectnummi.ui.theme.NummiTheme

@Composable
fun ManagePeopleScreen(
        viewModel: ManagePeopleViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    ManagePeopleScreen(state = state.value, listener = { viewModel.handle(it) })
}

@Composable
fun ManagePeopleScreen(
        state: ManagePeopleState,
        listener: (ManagePeopleIntent) -> Unit,
) {
    val displayItems = state.people?.sortedBy { it.name } ?: listOf()

    CreatePersonDialog(
            isShown = true,
            state = state.createDialogState,
            listener = { listener(CreatePersonDialogAction(it)) },
    )

    Box {
        LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(NummiTheme.dimens.listItemSpacedBy),
                contentPadding = PaddingValues(NummiTheme.dimens.screenPadding),
                modifier = Modifier.fillMaxSize()
        ) {
            items(displayItems) { item ->
                PersonItem(
                        person = item,
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                )
            }
        }

        FloatingActionButton(
                backgroundColor = NummiTheme.colors.fab.main,
                contentColor = NummiTheme.colors.fab.content,
                onClick = { listener(AddPersonClicked) },
                modifier = Modifier
                        .padding(NummiTheme.dimens.fabToScreenEdgePadding)
                        .align(Alignment.BottomEnd)
        ) {
            Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add person",
            )
        }
    }
}

@Preview
@Composable
fun ManageCategoriesScreen_Preview() {
    NummiScreenPreviewWrapper {
        ManagePeopleScreen(
                state = ManagePeopleState(
                        people = PeopleProvider.basic,
                )
        ) {}
    }
}
