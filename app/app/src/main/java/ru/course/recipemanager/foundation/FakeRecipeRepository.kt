package ru.course.recipemanager.foundation

import ru.course.recipemanager.entity.CookingStep
import ru.course.recipemanager.entity.AdminStats
import ru.course.recipemanager.entity.Difficulty
import ru.course.recipemanager.entity.Ingredient
import ru.course.recipemanager.entity.Nutrition
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.entity.RecipeCategory
import ru.course.recipemanager.entity.ShoppingItem
import ru.course.recipemanager.entity.UserProfile
import ru.course.recipemanager.entity.UserRole
import ru.course.recipemanager.entity.UserSettings
import ru.course.recipemanager.foundation.local.ProfileStore

class FakeRecipeRepository(
    private val profileStore: ProfileStore? = null
) : RecipeRepository {
    private val savedProfiles = mutableMapOf<String, UserProfile>()
    private val recipes = mutableListOf(
        Recipe(
            id = 1,
            title = "Ягодная овсянка",
            category = RecipeCategory.Breakfast,
            description = "Нежный завтрак с ягодами, мятой и медом для спокойного утра.",
            cookingTimeMinutes = 15,
            portions = 2,
            difficulty = Difficulty.Easy,
            ingredients = listOf(
                Ingredient("Овсяные хлопья", "80", "г"),
                Ingredient("Молоко", "220", "мл"),
                Ingredient("Клубника и голубика", "120", "г"),
                Ingredient("Мед", "1", "ч. л.")
            ),
            steps = listOf(
                CookingStep(1, "Сварить овсянку на молоке до мягкой текстуры."),
                CookingStep(2, "Добавить мед, ягоды и листики мяты."),
                CookingStep(3, "Сохранить рецепт оффлайн и добавить ягоды в покупки.")
            ),
            nutrition = Nutrition(calories = 315, protein = 11, fat = 8, carbs = 52),
            imageUri = null,
            isFavorite = true,
            isOfflineAvailable = true
        ),
        Recipe(
            id = 2,
            title = "Паста с зеленью",
            category = RecipeCategory.Dinner,
            description = "Быстрый ужин с нежным соусом и свежей зеленью.",
            cookingTimeMinutes = 25,
            portions = 2,
            difficulty = Difficulty.Easy,
            ingredients = listOf(Ingredient("Паста", "180", "г"), Ingredient("Зелень", "1", "пучок")),
            steps = listOf(CookingStep(1, "Отварить пасту."), CookingStep(2, "Смешать с соусом и зеленью.")),
            nutrition = Nutrition(calories = 520, protein = 18, fat = 17, carbs = 74),
            imageUri = null,
            isFavorite = false,
            isOfflineAvailable = true
        ),
        Recipe(
            id = 3,
            title = "Сырники к чаю",
            category = RecipeCategory.Dessert,
            description = "Воздушные сырники с ягодной подачей.",
            cookingTimeMinutes = 30,
            portions = 3,
            difficulty = Difficulty.Medium,
            ingredients = listOf(Ingredient("Творог", "400", "г"), Ingredient("Яйцо", "1", "шт")),
            steps = listOf(CookingStep(1, "Смешать ингредиенты."), CookingStep(2, "Обжарить до румяности.")),
            nutrition = Nutrition(calories = 410, protein = 29, fat = 16, carbs = 35),
            imageUri = null,
            isFavorite = true,
            isOfflineAvailable = false
        )
    )

    private val shoppingItems = mutableListOf(
        ShoppingItem(1, "Овсяные хлопья — 80 г", true),
        ShoppingItem(2, "Молоко — 220 мл", true),
        ShoppingItem(3, "Клубника — 120 г", false),
        ShoppingItem(4, "Голубика — 120 г", false),
        ShoppingItem(5, "Творог — 400 г", false),
        ShoppingItem(6, "Мята — 1 пучок", false)
    )

    private var profile = UserProfile(
        name = "Гость",
        email = "guest@ladushki.app",
        role = UserRole.User,
        recipesCount = recipes.size,
        favoriteCount = recipes.count { it.isFavorite },
        offlineCount = recipes.count { it.isOfflineAvailable }
    )

    private var settings = UserSettings()

    override suspend fun login(email: String, password: String): UserProfile {
        require(email.isNotBlank()) { "Введите email" }
        require(password.length >= 4) { "Пароль должен быть не короче 4 символов" }

        val isAdmin = email.equals("admin@ladushki.app", ignoreCase = true)
        val freshProfile = UserProfile(
            name = if (isAdmin) "Администратор" else email.substringBefore("@").replaceFirstChar { it.uppercase() },
            email = email,
            role = if (isAdmin) UserRole.Admin else UserRole.User,
            recipesCount = recipes.size,
            favoriteCount = recipes.count { it.isFavorite },
            offlineCount = recipes.count { it.isOfflineAvailable }
        )
        val savedProfile = savedProfiles[email]
        val storedProfile = profileStore?.load(email)?.applyTo(freshProfile)
        profile = (savedProfile ?: storedProfile)?.copy(
            role = freshProfile.role,
            recipesCount = freshProfile.recipesCount,
            favoriteCount = freshProfile.favoriteCount,
            offlineCount = freshProfile.offlineCount
        ) ?: freshProfile
        savedProfiles[email] = profile
        profileStore?.save(profile)
        return profile
    }

    override suspend fun register(name: String, email: String, password: String): UserProfile {
        require(name.isNotBlank()) { "Введите имя" }
        return login(email, password).copy(name = name).also {
            profile = it
            savedProfiles[email] = it
            profileStore?.save(it)
        }
    }

    override suspend fun getRecipes(): List<Recipe> = recipes.toList()

    override suspend fun getRecipeById(id: Long): Recipe? = recipes.firstOrNull { it.id == id }

    override suspend fun getShoppingItems(): List<ShoppingItem> = shoppingItems.toList()

    override suspend fun getProfile(): UserProfile = profile.copy(
        recipesCount = recipes.size,
        favoriteCount = recipes.count { it.isFavorite },
        offlineCount = recipes.count { it.isOfflineAvailable }
    )

    override suspend fun updateProfile(name: String, avatarUri: String?): UserProfile {
        profile = profile.copy(
            name = name.ifBlank { profile.name },
            avatarUri = avatarUri
        )
        savedProfiles[profile.email] = profile
        profileStore?.save(profile)
        return getProfile()
    }

    override suspend fun getSettings(): UserSettings = settings

    override suspend fun updateSettings(settings: UserSettings): UserSettings {
        this.settings = settings
        return settings
    }

    override suspend fun addRecipe(recipe: Recipe): Recipe {
        val saved = recipe.copy(
            id = (recipes.maxOfOrNull { it.id } ?: 0L) + 1L,
            isOfflineAvailable = settings.offlineCacheEnabled
        )
        recipes.add(0, saved)
        return saved
    }

    override suspend fun toggleFavorite(recipeId: Long): Recipe? {
        val index = recipes.indexOfFirst { it.id == recipeId }
        if (index == -1) return null
        val updated = recipes[index].copy(isFavorite = !recipes[index].isFavorite)
        recipes[index] = updated
        return updated
    }

    override suspend fun deleteRecipe(recipeId: Long) {
        recipes.removeAll { it.id == recipeId }
    }

    override suspend fun toggleShoppingItem(itemId: Long): List<ShoppingItem> {
        val index = shoppingItems.indexOfFirst { it.id == itemId }
        if (index != -1) {
            shoppingItems[index] = shoppingItems[index].copy(isChecked = !shoppingItems[index].isChecked)
        }
        return shoppingItems.toList()
    }

    override suspend fun clearCompletedShoppingItems(): List<ShoppingItem> {
        shoppingItems.removeAll { it.isChecked }
        return shoppingItems.toList()
    }

    override suspend fun clearShoppingList(): List<ShoppingItem> {
        shoppingItems.clear()
        return emptyList()
    }

    override suspend fun addIngredientsToShoppingList(ingredients: List<Ingredient>) {
        val startId = (shoppingItems.maxOfOrNull { it.id } ?: 0) + 1
        ingredients.forEachIndexed { index, ingredient ->
            val displayName = ingredient.displayName()
            if (shoppingItems.none { it.name == displayName }) {
                shoppingItems += ShoppingItem(startId + index, displayName, false)
            }
        }
    }

    override suspend fun getAdminStats(): AdminStats = AdminStats(
        usersCount = 18,
        recipesCount = recipes.size,
        pendingRecipesCount = 3,
        reportsCount = 1
    )
}
