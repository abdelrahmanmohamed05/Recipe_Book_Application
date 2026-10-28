package com.example.myapplication.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "recipes")

class Recipe (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title : String,
    val description: String = "",
    val category: String = "",
    val prepTimeMinutes: Int = 0,
    val servings: Int = 1,
    val isFavorite: Boolean = false


)