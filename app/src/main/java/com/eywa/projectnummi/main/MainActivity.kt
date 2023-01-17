package com.eywa.projectnummi.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavArgument
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eywa.projectnummi.navigation.NummiNavRoute
import com.eywa.projectnummi.navigation.toNummiNavRoute
import com.eywa.projectnummi.theme.NummiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navRoutes = NummiNavRoute.values()
        val duplicateRoutes = navRoutes
                .groupBy { it.routeBase.lowercase() }
                .filter { (_, v) -> v.size > 1 }
                .keys
        if (duplicateRoutes.isNotEmpty()) {
            throw IllegalStateException("Duplicate navRoutes found: " + duplicateRoutes.joinToString(","))
        }

        setContent {
            NummiTheme {
                val navController = rememberNavController()
                val currentEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentEntry?.destination?.route

                window.navigationBarColor = NummiTheme.colors.androidNavButtons.toArgb()
                window.statusBarColor = NummiTheme.colors.statusBar.toArgb()

                Scaffold(
                        backgroundColor = NummiTheme.colors.appBackground.main,
                        bottomBar = {
                            NummiBottomNav(
                                    currentRoute = currentRoute,
                                    onClick = {
                                        it.navigate(navController) {
                                            fun saveState(route: String?, args: Map<String, NavArgument>?) =
                                                    it.routeBase != currentRoute
                                                            && route
                                                            ?.toNummiNavRoute()
                                                            ?.saveStateOnNavigate(args)
                                                            ?: false

                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState =
                                                        saveState(currentRoute, currentEntry?.destination?.arguments)
                                            }
                                            restoreState = saveState(it.routeBase, null)
                                            launchSingleTop = true
                                        }
                                    },
                            )
                        }
                ) { padding ->
                    NavHost(
                            navController = navController,
                            startDestination = NummiNavRoute.VIEW_USER_TRANSACTIONS.routeBase,
                            modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
                    ) {
                        navRoutes.forEach { route ->
                            route.create(this, navController)
                        }
                    }
                }
            }
        }
    }
}
