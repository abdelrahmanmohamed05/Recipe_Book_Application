package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.RecipeRepository
import com.example.myapplication.data.RecipeWithDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val repository: RecipeRepository,
    private val recipeId: Int
) : ViewModel() {

    // Expose recipe as a read-only StateFlow, starting with null while loading or if not found
    val recipe: StateFlow<RecipeWithDetails?> = repository.getRecipe(recipeId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Toggle favorite status of this specific recipe
    fun toggleFavorite() {
        viewModelScope.launch {
            repository.toggleFavorite(recipeId)
        }
    }

    // Delete the recipe using the loaded recipe entity if available
    fun deleteRecipe() {
        val currentRecipe = recipe.value?.recipe ?: return
        viewModelScope.launch {
            repository.deleteRecipe(currentRecipe)
        }
    }
}
