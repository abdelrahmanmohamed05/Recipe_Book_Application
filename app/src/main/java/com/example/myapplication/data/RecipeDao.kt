package com.example.myapplication.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: Recipe): Long

    @Insert
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    @Insert
    suspend fun insertSteps(steps: List<Step>)

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%'")
    suspend fun searchByTitle(query: String): List<Recipe>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    suspend fun getFavorites(): List<Recipe>

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun count(): Int

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Query("UPDATE recipes SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Int)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("DELETE FROM ingredients WHERE recipeId = :recipeId")
    suspend fun deleteIngredientsOf(recipeId: Int)

    @Query("DELETE FROM steps WHERE recipeId = :recipeId")
    suspend fun deleteStepsOf(recipeId: Int)

    @Query("SELECT * FROM recipes")
    suspend fun getRecipesWithDetails(): List<RecipeWithDetails>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeWithDetails(id: Int): RecipeWithDetails?

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%'")
    suspend fun searchWithDetails(query: String): List<RecipeWithDetails>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    suspend fun getFavoritesWithDetails(): List<RecipeWithDetails>






}