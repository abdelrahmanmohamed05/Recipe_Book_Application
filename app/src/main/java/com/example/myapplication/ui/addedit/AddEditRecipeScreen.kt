package com.example.myapplication.ui.addedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    form: RecipeForm,
    errors: FormErrors,
    isEditMode: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onPrepTimeChange: (String) -> Unit,
    onServingsChange: (String) -> Unit,
    onIngredientAdd: () -> Unit,
    onIngredientChange: (Int, IngredientInput) -> Unit,
    onIngredientRemove: (Int) -> Unit,
    onStepAdd: () -> Unit,
    onStepChange: (Int, String) -> Unit,
    onStepRemove: (Int) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Recipe" else "Add Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { FormField("Title", form.title, onTitleChange, errors.title) }
            item { FormField("Description", form.description, onDescriptionChange) }
            item { FormField("Category", form.category, onCategoryChange) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FormField(
                        label = "Prep time (min)",
                        value = form.prepTime,
                        onValueChange = onPrepTimeChange,
                        isNumber = true,
                        modifier = Modifier.weight(1f)
                    )
                    FormField(
                        label = "Servings",
                        value = form.servings,
                        onValueChange = onServingsChange,
                        isNumber = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { SectionHeader("Ingredients", errors.ingredients) }
            itemsIndexed(form.ingredients) { index, ingredient ->
                IngredientRow(
                    ingredient = ingredient,
                    onChange = { onIngredientChange(index, it) },
                    onRemove = { onIngredientRemove(index) }
                )
            }
            item { AddRowButton("Add ingredient", onIngredientAdd) }

            item { SectionHeader("Steps", errors.steps) }
            itemsIndexed(form.steps) { index, step ->
                StepRow(
                    number = index + 1,
                    text = step,
                    onChange = { onStepChange(index, it) },
                    onRemove = { onStepRemove(index) }
                )
            }
            item { AddRowButton("Add step", onStepAdd) }

            item {
                Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                    Text("Save")
                }
            }
            if (isEditMode) {
                item {
                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete recipe?") },
            text = { Text("This recipe will be deleted permanently.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    isNumber: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        singleLine = isNumber,
        modifier = modifier
    )
}

@Composable
private fun SectionHeader(title: String, error: String?) {
    Text(title, style = MaterialTheme.typography.titleMedium)
    if (error != null) {
        Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun IngredientRow(
    ingredient: IngredientInput,
    onChange: (IngredientInput) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = ingredient.name,
            onValueChange = { onChange(ingredient.copy(name = it)) },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.weight(2f)
        )
        OutlinedTextField(
            value = ingredient.quantity,
            onValueChange = { onChange(ingredient.copy(quantity = it)) },
            label = { Text("Qty") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRemove) {
            Icon(Icons.Filled.Close, contentDescription = "Remove ingredient")
        }
    }
}

@Composable
private fun StepRow(
    number: Int,
    text: String,
    onChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("$number.", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = text,
            onValueChange = onChange,
            label = { Text("Step $number") },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRemove) {
            Icon(Icons.Filled.Close, contentDescription = "Remove step")
        }
    }
}

@Composable
private fun AddRowButton(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Icon(Icons.Filled.Add, contentDescription = null)
        Text(text)
    }
}

@Composable
private fun PreviewScreen(form: RecipeForm, isEditMode: Boolean) {
    MaterialTheme {
        AddEditRecipeScreen(
            form = form,
            errors = FormErrors(),
            isEditMode = isEditMode,
            onTitleChange = {},
            onDescriptionChange = {},
            onCategoryChange = {},
            onPrepTimeChange = {},
            onServingsChange = {},
            onIngredientAdd = {},
            onIngredientChange = { _, _ -> },
            onIngredientRemove = {},
            onStepAdd = {},
            onStepChange = { _, _ -> },
            onStepRemove = {},
            onSave = {},
            onDelete = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddRecipePreview() {
    PreviewScreen(form = RecipeForm(), isEditMode = false)
}

@Preview(showBackground = true)
@Composable
private fun EditRecipePreview() {
    PreviewScreen(
        form = RecipeForm(
            title = "Pancakes",
            description = "Soft breakfast pancakes",
            category = "Breakfast",
            prepTime = "20",
            servings = "4",
            ingredients = listOf(IngredientInput("Flour", "2 cups"), IngredientInput("Milk", "1 cup")),
            steps = listOf("Mix the ingredients", "Cook on a hot pan")
        ),
        isEditMode = true
    )
}
