package ru.course.recipemanager.mediator

import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.entity.UserSettings
import ru.course.recipemanager.foundation.RecipeRepository

class RecipeInteractor(
    private val repository: RecipeRepository
) {
    suspend fun login(email: String, password: String) = repository.login(email, password)

    suspend fun register(name: String, email: String, password: String) = repository.register(name, email, password)

    suspend fun loadRecipes() = repository.getRecipes()

    suspend fun loadShoppingItems() = repository.getShoppingItems()

    suspend fun loadProfile() = repository.getProfile()

    suspend fun updateProfile(name: String, avatarUri: String?) = repository.updateProfile(name, avatarUri)

    suspend fun loadSettings() = repository.getSettings()

    suspend fun saveSettings(settings: UserSettings) = repository.updateSettings(settings)

    suspend fun addRecipe(recipe: Recipe) = repository.addRecipe(recipe)

    suspend fun toggleFavorite(recipeId: Long) = repository.toggleFavorite(recipeId)

    suspend fun deleteRecipe(recipeId: Long) = repository.deleteRecipe(recipeId)

    suspend fun toggleShoppingItem(itemId: Long) = repository.toggleShoppingItem(itemId)

    suspend fun clearCompletedShoppingItems() = repository.clearCompletedShoppingItems()

    suspend fun clearShoppingList() = repository.clearShoppingList()

    suspend fun loadAdminStats() = repository.getAdminStats()

    suspend fun addIngredientsToShoppingList(recipeId: Long) {
        val recipe: Recipe = repository.getRecipeById(recipeId) ?: return
        repository.addIngredientsToShoppingList(recipe.ingredients)
    }
}
