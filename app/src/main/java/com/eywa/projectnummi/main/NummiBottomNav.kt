package com.eywa.projectnummi.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.eywa.projectnummi.ui.theme.NummiTheme
import com.eywa.projectnummi.ui.utils.NummiIconInfo

@Composable
fun NummiBottomNav(
        navController: NavController,
        onClick: (destination: NavRoute) -> Unit,
) {
    val current by navController.currentBackStackEntryAsState()
    NummiBottomNav(
            currentRoute = current?.destination?.route,
            onClick = onClick,
    )
}

@Composable
fun NummiBottomNav(
        currentRoute: String?,
        onClick: (destination: NavRoute) -> Unit,
) {
    BottomNavigation(
            backgroundColor = NummiTheme.colors.navBar.main,
            contentColor = NummiTheme.colors.navBar.content,
    ) {
        NummiBottomNavItem(
                icon = NummiIconInfo.VectorIcon(Icons.Outlined.Add),
                selectedIcon = NummiIconInfo.VectorIcon(Icons.Filled.Add),
                label = "Add",
                contentDescription = "Add transactions",
                destination = MainNavRoute.ADD_TRANSACTIONS,
                currentRoute = currentRoute,
                onClick = onClick,
        )
        NummiBottomNavItem(
                icon = NummiIconInfo.VectorIcon(Icons.Outlined.List),
                selectedIcon = NummiIconInfo.VectorIcon(Icons.Filled.List),
                label = "View",
                contentDescription = "View transactions",
                destination = MainNavRoute.VIEW_TRANSACTIONS,
                currentRoute = currentRoute,
                onClick = onClick,
        )
    }
}


@Composable
fun RowScope.NummiBottomNavItem(
        icon: NummiIconInfo,
        selectedIcon: NummiIconInfo = icon,
        label: String,
        contentDescription: String,
        badgeContent: String? = null,
        destination: NavRoute,
        currentRoute: String?,
        onClick: (destination: NavRoute) -> Unit,
) = NummiBottomNavItem(
        icon = icon,
        selectedIcon = selectedIcon,
        label = label,
        contentDescription = contentDescription,
        badgeContent = badgeContent,
        destinations = listOf(destination),
        currentRoute = currentRoute,
        onClick = onClick,
)

/**
 * @param destinations the first destination is where it will navigate to on click,
 * others are used to determine whether the item is selected
 */
@Composable
fun RowScope.NummiBottomNavItem(
        icon: NummiIconInfo,
        selectedIcon: NummiIconInfo = icon,
        label: String,
        contentDescription: String,
        badgeContent: String? = null,
        destinations: Iterable<NavRoute>,
        currentRoute: String?,
        onClick: (destination: NavRoute) -> Unit,
) {
    require(destinations.any()) { "No destinations for nav button" }
    val isSelected = destinations.map { it.routeBase }.contains(currentRoute)
    BottomNavigationItem(
            selected = isSelected,
            onClick = { onClick(destinations.first()) },
            icon = {
                val displayIcon = if (isSelected) selectedIcon else icon
                if (badgeContent != null) {
                    BadgedBox(
                            badge = {
                                Badge(content = badgeContent.takeIf { it.isNotBlank() }?.let { { Text(badgeContent) } })
                            },
                            content = { displayIcon.NummiIcon() },
                    )
                }
                else {
                    displayIcon.NummiIcon()
                }
            },
            label = {
                Text(
                        text = label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                )
            },
            modifier = Modifier.semantics { this.contentDescription = contentDescription }
    )
}