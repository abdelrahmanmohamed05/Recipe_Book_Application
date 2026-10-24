package com.example.myapplication.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "steps",
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["id"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE
    )]
)


class Step(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val recipeId: Int,
    val stepNumber: Int,
    val instruction: String

)