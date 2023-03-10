package com.eywa.projectnummi.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.eywa.projectnummi.R
import com.eywa.projectnummi.navigation.NummiNavRoute
import com.eywa.projectnummi.sharedUi.utils.NummiIconInfo
import com.eywa.projectnummi.theme.NummiTheme

@Composable
fun NummiBottomNav(
        currentRoute: String?,
        onClick: (destination: NummiNavRoute) -> Unit,
) {
    BottomNavigation(
            backgroundColor = NummiTheme.colors.navBar.main,
            contentColor = NummiTheme.colors.navBar.content,
    ) {
        NummiBottomNavItem(
                icon = NummiIconInfo.VectorIcon(Icons.Default.List),
                label = "View",
                contentDescription = "View transactions",
                destination = NummiNavRoute.VIEW_USER_TRANSACTIONS,
                currentRoute = currentRoute,
                onClick = onClick,
        )
        NummiBottomNavItem(
                icon = NummiIconInfo.PainterIcon(R.drawable.ic_donut_large),
                label = "Summary",
                contentDescription = "Transactions summary",
                destination = NummiNavRoute.TRANSACTIONS_SUMMARY,
                currentRoute = currentRoute,
                onClick = onClick,
        )
        NummiBottomNavItem(
                icon = NummiIconInfo.PainterIcon(R.drawable.ic_category_outline),
                selectedIcon = NummiIconInfo.PainterIcon(R.drawable.ic_category_baseline),
                label = "Manage",
                contentDescription = "Manage saved transactions, categories, people, and accounts",
                destinations = listOf(
                        NummiNavRoute.VIEW_RECURRING_TRANSACTIONS,
                        NummiNavRoute.MANAGE_CATEGORIES,
                        NummiNavRoute.MANAGE_PEOPLE,
                        NummiNavRoute.MANAGE_ACCOUNTS,
                ),
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
        destination: NummiNavRoute,
        currentRoute: String?,
        onClick: (destination: NummiNavRoute) -> Unit,
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
        destinations: Iterable<NummiNavRoute>,
        currentRoute: String?,
        onClick: (destination: NummiNavRoute) -> Unit,
) {
    require(destinations.any()) { "No destinations for nav button" }
    val isSelected = destinations.any { currentRoute?.contains(it.routeBase) == true }
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
            modifier = Modifier
                    .semantics { this.contentDescription = contentDescription }
                    .scale(if (isSelected) 1.15f else 1.0f)
    )
}
