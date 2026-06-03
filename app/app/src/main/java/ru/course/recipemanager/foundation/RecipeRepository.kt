package ru.course.recipemanager.foundation

import ru.course.recipemanager.entity.Ingredient
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.entity.ShoppingItem
import ru.course.recipemanager.entity.AdminStats
import ru.course.recipemanager.entity.UserSettings
import ru.course.recipemanager.entity.UserProfile

interface RecipeRepository {
    suspend fun login(email: String, password: String): UserProfile
    suspend fun register(name: String, email: String, password: String): UserProfile
    suspend fun getRecipes(): List<Recipe>
    suspend fun getRecipeById(id: Long): Recipe?
    suspend fun getShoppingItems(): List<ShoppingItem>
    suspend fun getProfile(): UserProfile
    suspend fun updateProfile(name: String, avatarUri: String?): UserProfile
    suspend fun getSettings(): UserSettings
    suspend fun updateSettings(settings: UserSettings): UserSettings
    suspend fun addRecipe(recipe: Recipe): Recipe
    suspend fun toggleFavorite(recipeId: Long): Recipe?
    suspend fun deleteRecipe(recipeId: Long)
    suspend fun toggleShoppingItem(itemId: Long): List<ShoppingItem>
    suspend fun clearCompletedShoppingItems(): List<ShoppingItem>
    suspend fun clearShoppingList(): List<ShoppingItem>
    suspend fun addIngredientsToShoppingList(ingredients: List<Ingredient>)
    suspend fun getAdminStats(): AdminStats
}
