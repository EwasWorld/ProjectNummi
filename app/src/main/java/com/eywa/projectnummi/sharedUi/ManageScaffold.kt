package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.eywa.projectnummi.model.HasName
import com.eywa.projectnummi.navigation.NummiNavRoute
import com.eywa.projectnummi.sharedUi.utils.ManageTabSwitcherItem
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun <T : HasName> ManageScaffold(
        displayItems: List<T>,
        keepOrder: Boolean = false,
        addFabContentDescription: String,
        onAddFabClicked: () -> Unit,
        currentTab: ManageTabSwitcherItem,
        onTabSwitcherClicked: (ManageTabSwitcherItem) -> Unit,
        onItemClicked: (T?) -> Unit,
        itemContent: @Composable (T?) -> Unit,
) {
    Column(
            modifier = Modifier.fillMaxSize()
    ) {
        TabSwitcher(
                items = ManageTabSwitcherItem.values().toList(),
                selectedItem = currentTab,
                itemClickedListener = onTabSwitcherClicked,
        )

        Divider(color = NummiTheme.colors.divider)
        Box(
                modifier = Modifier.weight(1f)
        ) {
            ItemList(
                    items = displayItems,
                    keepOrder = keepOrder,
                    contentPadding = PaddingValues(NummiTheme.dimens.screenPadding),
            ) {
                BorderedItem(
                        onClick = { onItemClicked(it) },
                        content = { itemContent(it) },
                        modifier = Modifier.fillMaxWidth()
                )
            }

            FloatingActionButton(
                    backgroundColor = NummiTheme.colors.fab.main,
                    contentColor = NummiTheme.colors.fab.content,
                    onClick = onAddFabClicked,
                    modifier = Modifier
                            .padding(NummiTheme.dimens.fabToScreenEdgePadding)
                            .align(Alignment.BottomEnd)
            ) {
                Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = addFabContentDescription,
                )
            }
        }
    }
}

fun navigateToManageTab(route: NummiNavRoute, navController: NavController) {
    route.navigate(navController) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = false
        }
        restoreState = false
        launchSingleTop = true
    }
}
