package ru.course.recipemanager.foundation

import kotlinx.coroutines.flow.first
import ru.course.recipemanager.entity.AdminStats
import ru.course.recipemanager.entity.Ingredient
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.entity.ShoppingItem
import ru.course.recipemanager.entity.UserProfile
import ru.course.recipemanager.entity.UserRole
import ru.course.recipemanager.entity.UserSettings
import ru.course.recipemanager.foundation.local.RecipeDao
import ru.course.recipemanager.foundation.local.TokenStore
import ru.course.recipemanager.foundation.remote.LoginRequest
import ru.course.recipemanager.foundation.remote.RecipeApi
import ru.course.recipemanager.foundation.remote.RegisterRequest

class CachedRecipeRepository(
    private val api: RecipeApi,
    private val dao: RecipeDao,
    private val tokenStore: TokenStore? = null
) : RecipeRepository {
    private var settings = UserSettings()

    override suspend fun login(email: String, password: String): UserProfile {
        val auth = api.login(LoginRequest(email, password))
        tokenStore?.saveAccessToken(auth.accessToken)
        return api.getProfile().toDomain().copy(email = email)
    }

    override suspend fun register(name: String, email: String, password: String): UserProfile {
        val auth = api.register(RegisterRequest(name, email, password))
        tokenStore?.saveAccessToken(auth.accessToken)
        return api.getProfile().toDomain().copy(name = name, email = email)
    }

    override suspend fun getRecipes(): List<Recipe> {
        return runCatching {
            val remote = api.getRecipes().map { it.toDomain() }
            dao.saveRecipes(remote.map { it.toCacheEntity() })
            remote
        }.getOrElse {
            dao.observeRecipes().first().map { cached -> cached.toDomain() }
        }
    }

    override suspend fun getRecipeById(id: Long): Recipe? {
        return runCatching {
            api.getRecipe(id).toDomain()
        }.getOrElse {
            dao.getRecipe(id)?.toDomain()
        }
    }

    override suspend fun getShoppingItems(): List<ShoppingItem> {
        return runCatching {
            api.getShoppingList().map { it.toDomain() }
        }.getOrElse {
            dao.observeShoppingItems().first().map { it.toDomain() }
        }
    }

    override suspend fun getProfile(): UserProfile {
        return runCatching {
            api.getProfile().toDomain()
        }.getOrDefault(UserProfile("Пользователь", "user@ladushki.app", UserRole.User, 0, 0, 0))
    }

    override suspend fun updateProfile(name: String, avatarUri: String?): UserProfile {
        return getProfile().copy(name = name.ifBlank { getProfile().name }, avatarUri = avatarUri)
    }

    override suspend fun getSettings(): UserSettings {
        return runCatching {
            api.getSettings().toDomain().also { settings = it }
        }.getOrDefault(settings)
    }

    override suspend fun updateSettings(settings: UserSettings): UserSettings {
        return runCatching {
            api.updateSettings(settings.toDto()).toDomain()
        }.getOrDefault(settings).also {
            this.settings = it
        }
    }

    override suspend fun addRecipe(recipe: Recipe): Recipe {
        return runCatching {
            api.createRecipe(recipe.toCreateRequest()).toDomain()
        }.getOrElse {
            recipe
        }
    }

    override suspend fun toggleFavorite(recipeId: Long): Recipe? {
        val current = getRecipeById(recipeId)
        runCatching {
            if (current?.isFavorite == true) {
                api.removeFavorite(recipeId)
            } else {
                api.addFavorite(recipeId)
            }
        }
        return getRecipeById(recipeId)
    }

    override suspend fun deleteRecipe(recipeId: Long) {
        api.deleteRecipe(recipeId)
    }

    override suspend fun toggleShoppingItem(itemId: Long): List<ShoppingItem> {
        runCatching { api.toggleShoppingItem(itemId) }
        return getShoppingItems()
    }

    override suspend fun clearCompletedShoppingItems(): List<ShoppingItem> {
        runCatching { api.clearCompletedShoppingItems() }
        return getShoppingItems().filterNot { it.isChecked }
    }

    override suspend fun clearShoppingList(): List<ShoppingItem> {
        runCatching { api.clearShoppingList() }
        return emptyList()
    }

    override suspend fun addIngredientsToShoppingList(ingredients: List<Ingredient>) {
        // В полной реализации здесь будет POST /shopping-list/from-recipe/{recipeId}.
        // Локальное сохранение остается на случай оффлайн-сценария.
    }

    override suspend fun getAdminStats(): AdminStats {
        return runCatching {
            api.getAdminStats().toDomain()
        }.getOrDefault(AdminStats(0, getRecipes().size, 0, 0))
    }
}
