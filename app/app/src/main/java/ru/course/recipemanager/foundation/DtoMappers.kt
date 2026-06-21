package ru.course.recipemanager.foundation

import ru.course.recipemanager.entity.CookingStep
import ru.course.recipemanager.entity.Difficulty
import ru.course.recipemanager.entity.Ingredient
import ru.course.recipemanager.entity.Nutrition
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.entity.RecipeCategory
import ru.course.recipemanager.entity.ShoppingItem
import ru.course.recipemanager.entity.UserProfile
import ru.course.recipemanager.entity.UserRole
import ru.course.recipemanager.entity.UserSettings
import ru.course.recipemanager.entity.AdminStats
import ru.course.recipemanager.foundation.local.RecipeCacheEntity
import ru.course.recipemanager.foundation.local.ShoppingCacheEntity
import ru.course.recipemanager.foundation.remote.AdminStatsDto
import ru.course.recipemanager.foundation.remote.CookingStepDto
import ru.course.recipemanager.foundation.remote.CreateRecipeRequest
import ru.course.recipemanager.foundation.remote.IngredientDto
import ru.course.recipemanager.foundation.remote.RecipeDto
import ru.course.recipemanager.foundation.remote.ShoppingItemDto
import ru.course.recipemanager.foundation.remote.UserProfileDto
import ru.course.recipemanager.foundation.remote.UserSettingsDto

fun RecipeDto.toDomain(): Recipe = Recipe(
    id = id,
    title = title,
    category = category.toRecipeCategory(),
    description = description,
    cookingTimeMinutes = cookingTimeMinutes,
    portions = portions,
    difficulty = difficulty.toDifficulty(),
    ingredients = ingredients.map { Ingredient(it.name, it.amount, it.unit) },
    steps = steps.map { CookingStep(it.number, it.text) },
    nutrition = Nutrition(calories, protein, fat, carbs),
    imageUri = imageUri,
    isFavorite = favorite,
    isOfflineAvailable = offlineAvailable
)

fun Recipe.toCacheEntity(): RecipeCacheEntity = RecipeCacheEntity(
    id = id,
    title = title,
    category = category.name,
    description = description,
    cookingTimeMinutes = cookingTimeMinutes,
    portions = portions,
    difficulty = difficulty.name,
    ingredientsText = ingredients.joinToString("\n") { "${it.name}|${it.amount}|${it.unit}" },
    stepsText = steps.joinToString("\n") { "${it.number}|${it.text}" },
    calories = nutrition.calories,
    protein = nutrition.protein,
    fat = nutrition.fat,
    carbs = nutrition.carbs,
    imageUri = imageUri,
    favorite = isFavorite,
    offlineAvailable = isOfflineAvailable
)

fun Recipe.toCreateRequest(): CreateRecipeRequest = CreateRecipeRequest(
    title = title,
    category = category.name,
    description = description,
    cookingTimeMinutes = cookingTimeMinutes,
    portions = portions,
    ingredients = ingredients.map { IngredientDto(it.name, it.amount, it.unit) },
    steps = steps.map { CookingStepDto(it.number, it.text) },
    calories = nutrition.calories,
    protein = nutrition.protein,
    fat = nutrition.fat,
    carbs = nutrition.carbs,
    imageUri = imageUri
)

fun RecipeCacheEntity.toDomain(): Recipe = Recipe(
    id = id,
    title = title,
    category = category.toRecipeCategory(),
    description = description,
    cookingTimeMinutes = cookingTimeMinutes,
    portions = portions,
    difficulty = difficulty.toDifficulty(),
    ingredients = ingredientsText.lines().filter { it.isNotBlank() }.map {
        val parts = it.split("|")
        Ingredient(parts.getOrElse(0) { "" }, parts.getOrElse(1) { "" }, parts.getOrElse(2) { "" })
    },
    steps = stepsText.lines().filter { it.isNotBlank() }.map {
        val parts = it.split("|")
        CookingStep(parts.getOrElse(0) { "0" }.toIntOrNull() ?: 0, parts.getOrElse(1) { "" })
    },
    nutrition = Nutrition(calories, protein, fat, carbs),
    imageUri = imageUri,
    isFavorite = favorite,
    isOfflineAvailable = offlineAvailable
)

fun ShoppingItemDto.toDomain(): ShoppingItem = ShoppingItem(id, name, checked)

fun ShoppingItem.toCacheEntity(): ShoppingCacheEntity = ShoppingCacheEntity(id, name, isChecked)

fun ShoppingCacheEntity.toDomain(): ShoppingItem = ShoppingItem(id, name, checked)

fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    name = name,
    email = email ?: "user@ladushki.app",
    role = if (role.equals("ADMIN", ignoreCase = true)) UserRole.Admin else UserRole.User,
    recipesCount = recipesCount,
    favoriteCount = favoriteCount,
    offlineCount = offlineCount
)

fun UserSettingsDto.toDomain(): UserSettings = UserSettings(
    syncEnabled = syncEnabled,
    offlineCacheEnabled = offlineCacheEnabled,
    notificationsEnabled = notificationsEnabled,
    compactModeEnabled = compactModeEnabled
)

fun UserSettings.toDto(): UserSettingsDto = UserSettingsDto(
    syncEnabled = syncEnabled,
    offlineCacheEnabled = offlineCacheEnabled,
    notificationsEnabled = notificationsEnabled,
    compactModeEnabled = compactModeEnabled
)

fun AdminStatsDto.toDomain(): AdminStats = AdminStats(
    usersCount = usersCount,
    recipesCount = recipesCount,
    pendingRecipesCount = pendingRecipesCount,
    reportsCount = reportsCount
)

private fun String.toRecipeCategory(): RecipeCategory =
    RecipeCategory.entries.firstOrNull { it.name.equals(this, true) || it.label.equals(this, true) } ?: RecipeCategory.Quick

private fun String.toDifficulty(): Difficulty =
    Difficulty.entries.firstOrNull { it.name.equals(this, true) || it.label.equals(this, true) } ?: Difficulty.Easy
