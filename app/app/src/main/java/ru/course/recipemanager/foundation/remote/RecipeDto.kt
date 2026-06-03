package ru.course.recipemanager.foundation.remote

data class RecipeDto(
    val id: Long,
    val title: String,
    val category: String,
    val description: String,
    val cookingTimeMinutes: Int,
    val portions: Int,
    val difficulty: String,
    val ingredients: List<IngredientDto>,
    val steps: List<CookingStepDto>,
    val calories: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val carbs: Int = 0,
    val imageUri: String? = null,
    val favorite: Boolean,
    val offlineAvailable: Boolean
)

data class IngredientDto(
    val name: String,
    val amount: String,
    val unit: String
)

data class CookingStepDto(
    val number: Int,
    val text: String
)

data class CreateRecipeRequest(
    val title: String,
    val category: String,
    val description: String,
    val cookingTimeMinutes: Int,
    val portions: Int,
    val ingredients: List<IngredientDto>,
    val steps: List<CookingStepDto>,
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int,
    val imageUri: String?
)

data class ShoppingItemDto(
    val id: Long,
    val name: String,
    val checked: Boolean
)

data class UserProfileDto(
    val name: String,
    val email: String? = null,
    val role: String? = null,
    val recipesCount: Int,
    val favoriteCount: Int,
    val offlineCount: Int
)

data class UserSettingsDto(
    val syncEnabled: Boolean,
    val offlineCacheEnabled: Boolean,
    val notificationsEnabled: Boolean,
    val compactModeEnabled: Boolean
)

data class AdminStatsDto(
    val usersCount: Int,
    val recipesCount: Int,
    val pendingRecipesCount: Int,
    val reportsCount: Int
)

data class ComplaintDto(
    val id: Long,
    val title: String,
    val recipeTitle: String,
    val description: String,
    val status: String
)
