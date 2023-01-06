package com.eywa.projectnummi.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eywa.projectnummi.ui.theme.NummiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NummiTheme {
                val navController = rememberNavController()
                val navRoutes = MainNavRoute.values()

                window.navigationBarColor = NummiTheme.colors.androidNavButtons.toArgb()
                window.statusBarColor = NummiTheme.colors.statusBar.toArgb()

                Scaffold(
                        backgroundColor = NummiTheme.colors.appBackground.main,
                        bottomBar = {
                            NummiBottomNav(
                                    navController = navController,
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
