package ru.course.recipemanager.control

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.course.recipemanager.entity.CookingStep
import ru.course.recipemanager.entity.Difficulty
import ru.course.recipemanager.entity.Ingredient
import ru.course.recipemanager.entity.Nutrition
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.entity.RecipeCategory
import ru.course.recipemanager.entity.UserSettings
import ru.course.recipemanager.foundation.FakeRecipeRepository
import ru.course.recipemanager.foundation.RecipeRepository
import ru.course.recipemanager.foundation.local.ProfileStore
import ru.course.recipemanager.mediator.RecipeInteractor

class RecipeViewModel @JvmOverloads constructor(
    application: Application,
    repository: RecipeRepository = FakeRecipeRepository(ProfileStore(application.applicationContext))
) : AndroidViewModel(application) {
    private val interactor = RecipeInteractor(repository)
    private val _state = MutableStateFlow(RecipeUiState(isLoading = true))
    val state: StateFlow<RecipeUiState> = _state.asStateFlow()

    init {
        refreshPublicData()
    }

    fun refreshPublicData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val recipes = interactor.loadRecipes()
            _state.update {
                val pendingIds = it.pendingRecipeIds.onlyExistingIn(recipes)
                it.copy(
                    recipes = recipes,
                    selectedRecipe = recipes.firstOrNull(),
                    pendingRecipeIds = pendingIds,
                    shoppingItems = interactor.loadShoppingItems(),
                    settings = interactor.loadSettings(),
                    adminStats = interactor.loadAdminStats().copy(pendingRecipesCount = pendingIds.size),
                    isLoading = false
                )
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            runCatching { interactor.login(email.trim(), password) }
                .onSuccess { profile ->
                    val recipes = interactor.loadRecipes()
                    _state.update {
                        val pendingIds = it.pendingRecipeIds.onlyExistingIn(recipes)
                        it.copy(
                            profile = profile,
                            recipes = recipes,
                            selectedRecipe = recipes.firstOrNull(),
                            pendingRecipeIds = pendingIds,
                            shoppingItems = interactor.loadShoppingItems(),
                            settings = interactor.loadSettings(),
                            adminStats = interactor.loadAdminStats().copy(pendingRecipesCount = pendingIds.size),
                            isAuthenticated = true,
                            message = "Добро пожаловать, ${profile.name}"
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(message = error.message ?: "Не удалось войти") }
                }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            runCatching { interactor.register(name.trim(), email.trim(), password) }
                .onSuccess { profile ->
                    _state.update {
                        it.copy(
                            profile = profile,
                            isAuthenticated = true,
                            message = "Аккаунт создан"
                        )
                    }
                    refreshAfterMutation()
                }
                .onFailure { error ->
                    _state.update { it.copy(message = error.message ?: "Не удалось зарегистрироваться") }
                }
        }
    }

    fun logout() {
        _state.update {
            it.copy(
                isAuthenticated = false,
                message = "Вы вышли из аккаунта",
                selectedCategory = null,
                searchQuery = ""
            )
        }
    }

    fun updateProfile(name: String, avatarUri: String?) {
        viewModelScope.launch {
            val updated = interactor.updateProfile(name.trim(), avatarUri)
            _state.update {
                it.copy(
                    profile = updated,
                    message = "Профиль обновлен"
                )
            }
        }
    }

    fun selectRecipe(recipe: Recipe) {
        _state.update { it.copy(selectedRecipe = recipe) }
    }

    fun updateSearch(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun selectCategory(category: RecipeCategory?) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun addSelectedRecipeToShoppingList() {
        viewModelScope.launch {
            val recipe = _state.value.selectedRecipe ?: return@launch
            interactor.addIngredientsToShoppingList(recipe.id)
            _state.update {
                it.copy(
                    shoppingItems = interactor.loadShoppingItems(),
                    message = "Ингредиенты добавлены в покупки"
                )
            }
        }
    }

    fun toggleFavorite(recipeId: Long) {
        viewModelScope.launch {
            val updated = interactor.toggleFavorite(recipeId)
            refreshAfterMutation(selectedRecipe = updated ?: _state.value.selectedRecipe)
            _state.update { it.copy(message = if (updated?.isFavorite == true) "Добавлено в избранное" else "Убрано из избранного") }
        }
    }

    fun saveRecipe(
        title: String,
        categoryText: String,
        timeText: String,
        portionsText: String,
        ingredientsText: String,
        note: String,
        caloriesText: String,
        proteinText: String,
        fatText: String,
        carbsText: String,
        imageUri: String?
    ) {
        viewModelScope.launch {
            val recipe = Recipe(
                id = 0,
                title = title.ifBlank { "Новый рецепт" },
                category = parseCategory(categoryText),
                description = note.ifBlank { "Домашний рецепт пользователя." },
                cookingTimeMinutes = timeText.filter { it.isDigit() }.toIntOrNull() ?: 20,
                portions = portionsText.filter { it.isDigit() }.toIntOrNull() ?: 2,
                difficulty = Difficulty.Easy,
                ingredients = parseIngredients(ingredientsText),
                steps = listOf(
                    CookingStep(1, "Подготовить ингредиенты."),
                    CookingStep(2, note.ifBlank { "Приготовить и красиво подать." })
                ),
                nutrition = Nutrition(
                    calories = caloriesText.onlyDigitsOr(250),
                    protein = proteinText.onlyDigitsOr(10),
                    fat = fatText.onlyDigitsOr(8),
                    carbs = carbsText.onlyDigitsOr(30)
                ),
                imageUri = imageUri,
                isFavorite = false,
                isOfflineAvailable = _state.value.settings.offlineCacheEnabled
            )
            val saved = interactor.addRecipe(recipe)
            refreshAfterMutation(selectedRecipe = saved)
            _state.update {
                val pendingIds = it.pendingRecipeIds + saved.id
                it.copy(
                    pendingRecipeIds = pendingIds,
                    adminStats = it.adminStats.copy(pendingRecipesCount = pendingIds.size),
                    message = "Рецепт сохранен и отправлен на проверку"
                )
            }
        }
    }

    fun updateRecipe(
        originalRecipeId: Long,
        title: String,
        categoryText: String,
        timeText: String,
        portionsText: String,
        ingredientsText: String,
        note: String,
        caloriesText: String,
        proteinText: String,
        fatText: String,
        carbsText: String,
        imageUri: String?
    ) {
        viewModelScope.launch {
            val current = _state.value.recipes.firstOrNull { it.id == originalRecipeId } ?: return@launch
            interactor.deleteRecipe(originalRecipeId)
            val updated = current.copy(
                id = 0,
                title = title.ifBlank { current.title },
                category = parseCategory(categoryText),
                description = note.ifBlank { current.description },
                cookingTimeMinutes = timeText.onlyDigitsOr(current.cookingTimeMinutes),
                portions = portionsText.onlyDigitsOr(current.portions),
                ingredients = parseIngredients(ingredientsText),
                nutrition = Nutrition(
                    calories = caloriesText.onlyDigitsOr(current.nutrition.calories),
                    protein = proteinText.onlyDigitsOr(current.nutrition.protein),
                    fat = fatText.onlyDigitsOr(current.nutrition.fat),
                    carbs = carbsText.onlyDigitsOr(current.nutrition.carbs)
                ),
                imageUri = imageUri,
                isOfflineAvailable = _state.value.settings.offlineCacheEnabled
            )
            val saved = interactor.addRecipe(updated)
            refreshAfterMutation(selectedRecipe = saved)
            _state.update {
                val pendingIds = it.pendingRecipeIds + saved.id
                it.copy(
                    pendingRecipeIds = pendingIds,
                    adminStats = it.adminStats.copy(pendingRecipesCount = pendingIds.size),
                    message = "Рецепт обновлен и отправлен на проверку"
                )
            }
        }
    }

    fun toggleShoppingItem(itemId: Long) {
        viewModelScope.launch {
            val items = interactor.toggleShoppingItem(itemId)
            _state.update { it.copy(shoppingItems = items) }
        }
    }

    fun clearCompletedShoppingItems() {
        viewModelScope.launch {
            val items = interactor.clearCompletedShoppingItems()
            _state.update { it.copy(shoppingItems = items, message = "Купленные продукты очищены") }
        }
    }

    fun clearShoppingList() {
        viewModelScope.launch {
            val items = interactor.clearShoppingList()
            _state.update { it.copy(shoppingItems = items, message = "Список покупок очищен") }
        }
    }

    fun saveSettings(settings: UserSettings) {
        viewModelScope.launch {
            val saved = interactor.saveSettings(settings)
            _state.update {
                it.copy(
                    settings = saved,
                    isOfflineMode = saved.offlineCacheEnabled,
                    message = "Настройки сохранены"
                )
            }
        }
    }

    fun deleteRecipe(recipeId: Long) {
        viewModelScope.launch {
            interactor.deleteRecipe(recipeId)
            refreshAfterMutation()
            _state.update {
                val pendingIds = it.pendingRecipeIds - recipeId
                it.copy(
                    pendingRecipeIds = pendingIds,
                    adminStats = it.adminStats.copy(pendingRecipesCount = pendingIds.size),
                    message = "Рецепт удален администратором"
                )
            }
        }
    }

    fun approveRecipe(recipeId: Long) {
        _state.update {
            val pendingIds = it.pendingRecipeIds - recipeId
            it.copy(
                pendingRecipeIds = pendingIds,
                adminStats = it.adminStats.copy(pendingRecipesCount = pendingIds.size),
                message = "Рецепт одобрен и опубликован"
            )
        }
    }

    fun clearMessage() {
        _state.update { it.copy(message = null) }
    }

    private suspend fun refreshAfterMutation(selectedRecipe: Recipe? = null) {
        val recipes = interactor.loadRecipes()
        _state.update {
            val currentPendingIds = it.pendingRecipeIds.onlyExistingIn(recipes)
            it.copy(
                recipes = recipes,
                selectedRecipe = selectedRecipe ?: recipes.firstOrNull(),
                pendingRecipeIds = currentPendingIds,
                shoppingItems = interactor.loadShoppingItems(),
                profile = interactor.loadProfile(),
                adminStats = interactor.loadAdminStats().copy(pendingRecipesCount = currentPendingIds.size)
            )
        }
    }

    private fun parseCategory(value: String): RecipeCategory {
        return RecipeCategory.entries.firstOrNull {
            it.label.equals(value.trim(), ignoreCase = true) || it.name.equals(value.trim(), ignoreCase = true)
        } ?: RecipeCategory.Quick
    }

    private fun parseIngredients(value: String): List<Ingredient> {
        val structured = value.lineSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split("|").map { it.trim() }
                if (parts.size >= 3 && parts[0].isNotBlank()) {
                    Ingredient(parts[0], parts[1], parts[2])
                } else {
                    null
                }
            }
            .toList()

        if (structured.isNotEmpty()) return structured

        return value.split(",", "\n")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { Ingredient(it, "", "") }
            .ifEmpty { listOf(Ingredient("Ингредиент", "1", "шт")) }
    }

    private fun String.onlyDigitsOr(defaultValue: Int): Int =
        filter { it.isDigit() }.toIntOrNull() ?: defaultValue

    private fun Set<Long>.onlyExistingIn(recipes: List<Recipe>): Set<Long> =
        filter { id -> recipes.any { recipe -> recipe.id == id } }.toSet()
}
