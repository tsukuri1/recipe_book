package ru.course.recipemanager.entity

data class Recipe(
    val id: Long,
    val title: String,
    val category: RecipeCategory,
    val description: String,
    val cookingTimeMinutes: Int,
    val portions: Int,
    val difficulty: Difficulty,
    val ingredients: List<Ingredient>,
    val steps: List<CookingStep>,
    val nutrition: Nutrition,
    val imageUri: String?,
    val isFavorite: Boolean,
    val isOfflineAvailable: Boolean
) {
    fun canBeCookedQuickly(): Boolean = cookingTimeMinutes <= 25
}

data class Nutrition(
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int
) {
    fun shortLabel(): String = "$calories ккал · Б $protein · Ж $fat · У $carbs"
}

enum class RecipeCategory(val label: String) {
    Breakfast("Завтрак"),
    Dinner("Ужин"),
    Dessert("Десерт"),
    Quick("Быстро")
}

enum class Difficulty(val label: String) {
    Easy("Легко"),
    Medium("Средне"),
    Hard("Сложно")
}
