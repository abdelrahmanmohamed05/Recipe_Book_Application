package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.RecipeRepository
import com.example.myapplication.data.RecipeWithDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeListViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _favoritesOnly = MutableStateFlow(false)
    val favoritesOnly: StateFlow<Boolean> = _favoritesOnly.asStateFlow()

    init {
        // Populate initial recipes if database is empty on first launch
        viewModelScope.launch {
            repository.seedIfEmpty()
        }
    }

    val recipes: StateFlow<List<RecipeWithDetails>> = combine(
        repository.getAllRecipes(),
        _searchQuery,
        _favoritesOnly
    ) { allRecipes, query, onlyFavorites ->
        allRecipes.filter { item ->
            val matchesSearch = query.isBlank() ||
                    item.recipe.title.contains(query, ignoreCase = true) ||
                    item.recipe.description.contains(query, ignoreCase = true)
            val matchesFavorites = !onlyFavorites || item.recipe.isFavorite
            matchesSearch && matchesFavorites
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavoritesOnly() {
        _favoritesOnly.value = !_favoritesOnly.value
    }

    fun toggleFavorite(recipeId: Int) {
        viewModelScope.launch {
            // Room Flow automatically updates UI without manual reload
            repository.toggleFavorite(recipeId)
        }
    }
}
