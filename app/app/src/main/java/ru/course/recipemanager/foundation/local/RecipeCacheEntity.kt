package ru.course.recipemanager.foundation.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_cache")
data class RecipeCacheEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val category: String,
    val description: String,
    val cookingTimeMinutes: Int,
    val portions: Int,
    val difficulty: String,
    val ingredientsText: String,
    val stepsText: String,
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int,
    val imageUri: String?,
    val favorite: Boolean,
    val offlineAvailable: Boolean
)

@Entity(tableName = "shopping_items")
data class ShoppingCacheEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val checked: Boolean
)
