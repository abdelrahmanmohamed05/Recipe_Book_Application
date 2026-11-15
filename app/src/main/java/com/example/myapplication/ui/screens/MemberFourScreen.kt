package com.example.myapplication.ui.screens

import androidx.compose.runtime.Composable

/**
 * Member Four Screen - XML View Implementation
 * Features implemented using XML Layouts & RecyclerView:
 * - Home Screen (screen_home.xml) with RecyclerView and FAB
 * - Search Bar (view_search_bar.xml)
 * - Empty State (view_empty_state.xml)
 * - Recipe Card Item (item_recipe_card.xml)
 * - Favorites Screen (screen_favorites.xml)
 */
@Composable
fun MemberFourScreen(
    onNavigateToDetail: (Long) -> Unit = {},
    onNavigateToAddEdit: (Long?) -> Unit = {}
) {
    HomeScreen(
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToAddEdit = onNavigateToAddEdit
    )
}
