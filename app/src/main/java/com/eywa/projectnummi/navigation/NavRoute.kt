package com.eywa.projectnummi.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface NavRoute {
    val routeBase: String

    fun create(navGraphBuilder: NavGraphBuilder, navController: NavController)
}
