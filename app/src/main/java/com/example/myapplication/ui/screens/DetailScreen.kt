package com.example.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.detail.RecipeDetailScreen
import com.example.myapplication.viewmodel.RecipeDetailViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

@Composable
fun DetailScreen(
    recipeId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit
) {
    val context = LocalContext.current
    val viewModel: RecipeDetailViewModel = viewModel(
        factory = ViewModelFactory(context, recipeId.toInt())
    )
    val details by viewModel.recipe.collectAsState()

    RecipeDetailScreen(
        details = details,
        onBack = onBack,
        onEdit = onEdit,
        onToggleFavorite = { viewModel.toggleFavorite() },
        onDelete = {
            viewModel.deleteRecipe()
            onBack()
        }
    )
}
