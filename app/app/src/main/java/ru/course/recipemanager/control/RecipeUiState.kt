package ru.course.recipemanager.control

import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.entity.RecipeCategory
import ru.course.recipemanager.entity.ShoppingItem
import ru.course.recipemanager.entity.AdminStats
import ru.course.recipemanager.entity.UserRole
import ru.course.recipemanager.entity.UserSettings
import ru.course.recipemanager.entity.UserProfile

data class RecipeUiState(
    val recipes: List<Recipe> = emptyList(),
    val selectedRecipe: Recipe? = null,
    val shoppingItems: List<ShoppingItem> = emptyList(),
    val pendingRecipeIds: Set<Long> = emptySet(),
    val profile: UserProfile = UserProfile("Гость", "guest@ladushki.app", UserRole.User, 0, 0, 0),
    val settings: UserSettings = UserSettings(),
    val adminStats: AdminStats = AdminStats(0, 0, 0, 0),
    val isAuthenticated: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: RecipeCategory? = null,
    val isOfflineMode: Boolean = true,
    val isLoading: Boolean = false,
    val message: String? = null
) {
    val filteredRecipes: List<Recipe>
        get() = recipes.filter { recipe ->
            val byCategory = selectedCategory == null || recipe.category == selectedCategory
            val byQuery = searchQuery.isBlank() ||
                recipe.title.contains(searchQuery, ignoreCase = true) ||
                recipe.description.contains(searchQuery, ignoreCase = true)
            byCategory && byQuery
        }
}
