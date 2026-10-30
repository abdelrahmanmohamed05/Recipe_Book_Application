package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Ingredient
import com.example.myapplication.data.Recipe
import com.example.myapplication.data.RecipeRepository
import com.example.myapplication.data.Step
import com.example.myapplication.ui.addedit.FormErrors
import com.example.myapplication.ui.addedit.IngredientInput
import com.example.myapplication.ui.addedit.RecipeForm
import com.example.myapplication.ui.addedit.removeAtIndex
import com.example.myapplication.ui.addedit.replaceAt
import com.example.myapplication.ui.addedit.validate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AddEditRecipeUiState(
    val form: RecipeForm = RecipeForm(),
    val errors: FormErrors = FormErrors(),
    val isEditMode: Boolean = false,
    val isSaved: Boolean = false
)

class AddEditRecipeViewModel(
    private val repository: RecipeRepository,
    private val recipeId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditRecipeUiState())
    val uiState: StateFlow<AddEditRecipeUiState> = _uiState.asStateFlow()

    private var existingRecipe: Recipe? = null

    init {
        // Load existing recipe data if editing an existing recipe instead of creating a new one
        if (recipeId != 0) {
            viewModelScope.launch {
                val details = repository.getRecipe(recipeId).first()
                if (details != null) {
                    existingRecipe = details.recipe
                    val loadedForm = RecipeForm(
                        title = details.recipe.title,
                        description = details.recipe.description,
                        category = details.recipe.category,
                        prepTime = if (details.recipe.prepTimeMinutes > 0) details.recipe.prepTimeMinutes.toString() else "",
                        servings = if (details.recipe.servings > 0) details.recipe.servings.toString() else "1",
                        ingredients = details.ingredients.map { IngredientInput(it.name, it.quantity) }.ifEmpty { listOf(IngredientInput()) },
                        steps = details.steps.sortedBy { it.stepNumber }.map { it.instruction }.ifEmpty { listOf("") }
                    )
                    _uiState.value = _uiState.value.copy(form = loadedForm, isEditMode = true)
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(title = title),
            errors = _uiState.value.errors.copy(title = null)
        )
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(description = description)
        )
    }

    fun updateCategory(category: String) {
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(category = category)
        )
    }

    fun updatePrepTime(prepTime: String) {
        // Keep only digits for numeric input field
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(prepTime = prepTime.filter(Char::isDigit))
        )
    }

    fun updateServings(servings: String) {
        // Keep only digits for numeric input field
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(servings = servings.filter(Char::isDigit))
        )
    }

    fun addIngredient() {
        val updated = _uiState.value.form.ingredients + IngredientInput()
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(ingredients = updated),
            errors = _uiState.value.errors.copy(ingredients = null)
        )
    }

    fun updateIngredient(index: Int, ingredient: IngredientInput) {
        val updated = _uiState.value.form.ingredients.replaceAt(index, ingredient)
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(ingredients = updated)
        )
    }

    fun removeIngredient(index: Int) {
        val updated = _uiState.value.form.ingredients.removeAtIndex(index)
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(ingredients = updated)
        )
    }

    fun addStep() {
        val updated = _uiState.value.form.steps + ""
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(steps = updated),
            errors = _uiState.value.errors.copy(steps = null)
        )
    }

    fun updateStep(index: Int, text: String) {
        val updated = _uiState.value.form.steps.replaceAt(index, text)
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(steps = updated)
        )
    }

    fun removeStep(index: Int) {
        val updated = _uiState.value.form.steps.removeAtIndex(index)
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(steps = updated)
        )
    }

    fun validateForm(): Boolean {
        val validationErrors = validate(_uiState.value.form)
        _uiState.value = _uiState.value.copy(errors = validationErrors)
        return !validationErrors.hasErrors
    }

    fun save() {
        if (!validateForm()) return

        val form = _uiState.value.form
        val recipe = Recipe(
            id = if (recipeId != 0) recipeId else 0,
            title = form.title.trim(),
            description = form.description.trim(),
            category = form.category.trim(),
            prepTimeMinutes = form.prepTime.toIntOrNull() ?: 0,
            servings = form.servings.toIntOrNull() ?: 1,
            isFavorite = existingRecipe?.isFavorite ?: false
        )

        val ingredients = form.ingredients
            .filter { it.name.isNotBlank() }
            .map { Ingredient(recipeId = recipe.id, name = it.name.trim(), quantity = it.quantity.trim()) }

        val steps = form.steps
            .filter { it.isNotBlank() }
            .mapIndexed { index, instruction ->
                Step(recipeId = recipe.id, stepNumber = index + 1, instruction = instruction.trim())
            }

        viewModelScope.launch {
            // Update existing recipe in edit mode, or insert new recipe otherwise
            if (recipeId != 0) {
                repository.updateRecipe(recipe, ingredients, steps)
            } else {
                repository.addRecipe(recipe, ingredients, steps)
            }
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }
}
