package com.eywa.projectnummi.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eywa.projectnummi.ui.theme.NummiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NummiTheme {
                val navController = rememberNavController()
                val navRoutes = MainNavRoute.values()

                Scaffold(
                        backgroundColor = NummiTheme.colors.appBackground.main,
                        bottomBar = {
                            NummiBottomNav(
                                    currentRoute = navController.currentDestination?.route,
                                    onClick = { navController.navigate(it.routeBase) },
                            )
                        }
                ) { padding ->
                    NavHost(
                            navController = navController,
                            startDestination = MainNavRoute.VIEW_TRANSACTIONS.routeBase,
                            modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
                    ) {
                        navRoutes.forEach { route ->
                            composable(route.routeBase) {
                                route.Screen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
