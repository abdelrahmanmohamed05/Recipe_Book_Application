package com.example.myapplication.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")
    
    data object Detail : Screen("detail/{id}") {
        fun createRoute(id: Long) = "detail/$id"
    }
    
    data object AddEdit : Screen("addEdit/{id?}") {
        fun createRoute(id: Long? = null) = if (id != null) "addEdit/$id" else "addEdit/"
    }
}
