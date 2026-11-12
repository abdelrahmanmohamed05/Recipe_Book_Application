package com.example.myapplication.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val db: AppDatabase) {

    private val dao = db.recipeDao()

    fun getAllRecipes(): Flow<List<RecipeWithDetails>> = dao.getRecipesWithDetails()

    fun getFavorites(): Flow<List<RecipeWithDetails>> = dao.getFavoritesWithDetails()

    fun getRecipe(id: Int): Flow<RecipeWithDetails?> = dao.getRecipeWithDetails(id)

    fun search(query: String): Flow<List<RecipeWithDetails>> = dao.searchWithDetails(query)

    suspend fun addRecipe(
        recipe: Recipe,
        ingredients: List<Ingredient>,
        steps: List<Step>
    ): Int = db.withTransaction {
        val recipeId = dao.insertRecipe(recipe).toInt()
        dao.insertIngredients(ingredients.map { it.copy(recipeId = recipeId) })
        dao.insertSteps(steps.map { it.copy(recipeId = recipeId) })
        recipeId
    }

    suspend fun updateRecipe(
        recipe: Recipe,
        ingredients: List<Ingredient>,
        steps: List<Step>
    ) = db.withTransaction {
        dao.updateRecipe(recipe)
        dao.deleteIngredientsOf(recipe.id)
        dao.deleteStepsOf(recipe.id)
        dao.insertIngredients(ingredients.map { it.copy(recipeId = recipe.id) })
        dao.insertSteps(steps.map { it.copy(recipeId = recipe.id) })
    }

    suspend fun toggleFavorite(id: Int) = dao.toggleFavorite(id)
    suspend fun deleteRecipe(recipe: Recipe) = dao.deleteRecipe(recipe)

    suspend fun seedIfEmpty() {
        if (dao.count() > 0) return

        addRecipe(
            Recipe(
                title = "Koshari",
                description = "Egyptian street food classic",
                category = "Main",
                prepTimeMinutes = 45,
                servings = 4
            ),
            ingredients = listOf(
                Ingredient(recipeId = 0, name = "Rice", quantity = "1 cup"),
                Ingredient(recipeId = 0, name = "Lentils", quantity = "1 cup"),
                Ingredient(recipeId = 0, name = "Macaroni", quantity = "1 cup"),
                Ingredient(recipeId = 0, name = "Tomato sauce", quantity = "2 cups")
            ),
            steps = listOf(
                Step(recipeId = 0, stepNumber = 1, instruction = "Boil the lentils and rice."),
                Step(recipeId = 0, stepNumber = 2, instruction = "Boil the macaroni."),
                Step(recipeId = 0, stepNumber = 3, instruction = "Mix everything and top with sauce.")
            )
        )

        addRecipe(
            Recipe(
                title = "Pancakes",
                description = "Fluffy breakfast pancakes",
                category = "Breakfast",
                prepTimeMinutes = 20,
                servings = 2
            ),
            ingredients = listOf(
                Ingredient(recipeId = 0, name = "Flour", quantity = "1 cup"),
                Ingredient(recipeId = 0, name = "Milk", quantity = "1 cup"),
                Ingredient(recipeId = 0, name = "Egg", quantity = "1")
            ),
            steps = listOf(
                Step(recipeId = 0, stepNumber = 1, instruction = "Mix all ingredients into a batter."),
                Step(recipeId = 0, stepNumber = 2, instruction = "Cook on a hot pan until golden on both sides.")
            )
        )

        addRecipe(
            Recipe(
                title = "Greek Salad",
                description = "Fresh and light",
                category = "Salad",
                prepTimeMinutes = 10,
                servings = 2
            ),
            ingredients = listOf(
                Ingredient(recipeId = 0, name = "Cucumber", quantity = "2"),
                Ingredient(recipeId = 0, name = "Tomato", quantity = "3"),
                Ingredient(recipeId = 0, name = "Feta cheese", quantity = "100 g")
            ),
            steps = listOf(
                Step(recipeId = 0, stepNumber = 1, instruction = "Chop the vegetables."),
                Step(recipeId = 0, stepNumber = 2, instruction = "Add feta and olive oil, then toss.")
            )
        )
    }
}