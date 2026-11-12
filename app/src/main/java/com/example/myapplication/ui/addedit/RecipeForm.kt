package com.example.myapplication.ui.addedit

data class IngredientInput(
    val name: String = "",
    val quantity: String = ""
)

data class RecipeForm(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val prepTime: String = "",
    val servings: String = "",
    val ingredients: List<IngredientInput> = listOf(IngredientInput()),
    val steps: List<String> = listOf("")
)

data class FormErrors(
    val title: String? = null,
    val ingredients: String? = null,
    val steps: String? = null
) {
    val hasErrors: Boolean
        get() = title != null || ingredients != null || steps != null
}

fun validate(form: RecipeForm): FormErrors = FormErrors(
    title = if (form.title.isBlank()) "Title is required" else null,
    ingredients = if (form.ingredients.none { it.name.isNotBlank() }) "Add at least one ingredient" else null,
    steps = if (form.steps.none { it.isNotBlank() }) "Add at least one step" else null
)

fun <T> List<T>.replaceAt(index: Int, item: T): List<T> =
    mapIndexed { i, old -> if (i == index) item else old }

fun <T> List<T>.removeAtIndex(index: Int): List<T> =
    filterIndexed { i, _ -> i != index }
