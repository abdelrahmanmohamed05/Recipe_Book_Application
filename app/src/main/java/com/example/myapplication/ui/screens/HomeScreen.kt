package com.example.myapplication.ui.screens

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
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
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Composable
fun HomeScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAddEdit: (Long?) -> Unit,
    viewModel: RecipeListViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    )
) {
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val adapter = remember {
        RecipeAdapter(
            onRecipeClick = onNavigateToDetail,
            onFavoriteClick = { recipeId -> viewModel.toggleFavorite(recipeId) }
        )
    }

    adapter.submitList(recipes)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.screen_home, null, false)

            val etSearch = view.findViewById<EditText>(R.id.etSearchQuery)
            val rvRecipes = view.findViewById<RecyclerView>(R.id.rvRecipes)
            val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAddRecipe)

            rvRecipes.adapter = adapter

            fabAdd.setOnClickListener {
                onNavigateToAddEdit(null)
            }

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
            if (recipes.isEmpty()) {
                layoutEmpty?.visibility = View.VISIBLE
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
