package com.example.myapplication.ui.screens

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.components.RecipeAdapter
import com.example.myapplication.viewmodel.RecipeListViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

@Composable
fun FavoritesScreen(
    onNavigateToDetail: (Long) -> Unit,
    viewModel: RecipeListViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    )
) {
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val favoriteRecipes = remember(recipes) {
        recipes.filter { it.recipe.isFavorite }
    }

    val adapter = remember {
        RecipeAdapter(
            onRecipeClick = onNavigateToDetail,
            onFavoriteClick = { recipeId -> viewModel.toggleFavorite(recipeId) }
        )
    }

    adapter.submitList(favoriteRecipes)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.screen_favorites, null, false)

            val etSearch = view.findViewById<EditText>(R.id.etSearchQuery)
            val rvFavoriteRecipes = view.findViewById<RecyclerView>(R.id.rvFavoriteRecipes)

            rvFavoriteRecipes.adapter = adapter

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.setSearchQuery(s?.toString().orEmpty())
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            view
        },
        update = { view ->
            val layoutEmpty = view.findViewById<View>(R.id.layoutEmptyState)
            val tvEmptyMessage = view.findViewById<TextView>(R.id.tvEmptyMessage)

            if (favoriteRecipes.isEmpty()) {
                layoutEmpty?.visibility = View.VISIBLE
                tvEmptyMessage?.setText(R.string.no_favorites_found)
            } else {
                layoutEmpty?.visibility = View.GONE
            }

            val etSearch = view.findViewById<EditText>(R.id.etSearchQuery)
            if (etSearch != null && etSearch.text.toString() != searchQuery) {
                etSearch.setText(searchQuery)
                etSearch.setSelection(searchQuery.length)
            }
        }
    )
}
