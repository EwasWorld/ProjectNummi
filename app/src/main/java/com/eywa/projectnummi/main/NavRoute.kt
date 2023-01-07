package com.eywa.projectnummi.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface NavRoute {
    val routeBase: String

    @Composable
    fun Screen(navController: NavHostController)
}
