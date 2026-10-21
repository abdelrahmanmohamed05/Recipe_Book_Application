package com.example.myapplication.ui.addedit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeFormTest {

    private val validForm = RecipeForm(
        title = "Pancakes",
        ingredients = listOf(IngredientInput("Flour", "2 cups")),
        steps = listOf("Mix everything")
    )

    @Test
    fun validForm_hasNoErrors() {
        assertFalse(validate(validForm).hasErrors)
    }

    @Test
    fun emptyTitle_showsTitleError() {
        val errors = validate(validForm.copy(title = "  "))
        assertEquals("Title is required", errors.title)
        assertTrue(errors.hasErrors)
    }

    @Test
    fun noIngredients_showsIngredientError() {
        val errors = validate(validForm.copy(ingredients = listOf(IngredientInput())))
        assertNotNull(errors.ingredients)
    }

    @Test
    fun noSteps_showsStepError() {
        val errors = validate(validForm.copy(steps = emptyList()))
        assertNotNull(errors.steps)
    }

    @Test
    fun emptyForm_showsAllErrors() {
        val errors = validate(RecipeForm())
        assertNotNull(errors.title)
        assertNotNull(errors.ingredients)
        assertNotNull(errors.steps)
    }

    @Test
    fun replaceAt_editsOnlyThatRow() {
        val steps = listOf("a", "b", "c").replaceAt(1, "B")
        assertEquals(listOf("a", "B", "c"), steps)
    }

    @Test
    fun removeAtIndex_keepsOrder() {
        val steps = listOf("a", "b", "c").removeAtIndex(0)
        assertEquals(listOf("b", "c"), steps)
    }
}
