package com.example.myapplication.util

import android.content.Context
import android.content.Intent
import com.example.myapplication.data.RecipeWithDetails

fun buildRecipeText(item: RecipeWithDetails): String {
    val text = StringBuilder()
    text.appendLine(item.recipe.title)
    text.appendLine()

    text.appendLine("Ingredients:")
    item.ingredients.forEach { text.appendLine("- ${it.quantity} ${it.name}") }
    text.appendLine()

    text.appendLine("Steps:")
    item.steps.sortedBy { it.stepNumber }.forEachIndexed { i, step ->
        text.appendLine("${i + 1}. ${step.instruction}")
    }

    return text.toString()
}

fun shareRecipe(context: Context, item: RecipeWithDetails) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, item.recipe.title)
        putExtra(Intent.EXTRA_TEXT, buildRecipeText(item))
    }
    context.startActivity(Intent.createChooser(intent, "Share recipe"))
}
