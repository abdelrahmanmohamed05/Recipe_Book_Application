package com.example.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.addedit.AddEditRecipeScreen
import com.example.myapplication.viewmodel.AddEditRecipeViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

@Composable
fun AddEditScreen(
    recipeId: Long?,
    onBack: () -> Unit,
    onDeleted: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: AddEditRecipeViewModel = viewModel(
        factory = ViewModelFactory(context, (recipeId ?: 0L).toInt())
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onDeleted()
    }

    AddEditRecipeScreen(
        form = state.form,
        errors = state.errors,
        isEditMode = state.isEditMode,
        onTitleChange = viewModel::updateTitle,
        onDescriptionChange = viewModel::updateDescription,
        onCategoryChange = viewModel::updateCategory,
        onPrepTimeChange = viewModel::updatePrepTime,
        onServingsChange = viewModel::updateServings,
        onIngredientAdd = viewModel::addIngredient,
        onIngredientChange = viewModel::updateIngredient,
        onIngredientRemove = viewModel::removeIngredient,
        onStepAdd = viewModel::addStep,
        onStepChange = viewModel::updateStep,
        onStepRemove = viewModel::removeStep,
        onSave = viewModel::save,
        onDelete = viewModel::deleteRecipe,
        onBack = onBack
    )
}
