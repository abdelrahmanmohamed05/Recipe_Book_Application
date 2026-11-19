package com.example.myapplication.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.RecipeRepository

class ViewModelFactory(
    private val context: Context,
    private val recipeId: Int = 0
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContext = context.applicationContext

        if (modelClass.isAssignableFrom(RecipeListViewModel::class.java)) {
            val repository = RecipeRepository(AppDatabase.getInstance(appContext))
            @Suppress("UNCHECKED_CAST")
            return RecipeListViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            val repository = RecipeRepository(AppDatabase.getInstance(appContext))
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(repository, recipeId) as T
        } else if (modelClass.isAssignableFrom(AddEditRecipeViewModel::class.java)) {
            val repository = RecipeRepository(AppDatabase.getInstance(appContext))
            @Suppress("UNCHECKED_CAST")
            return AddEditRecipeViewModel(repository, recipeId) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(appContext as Application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
