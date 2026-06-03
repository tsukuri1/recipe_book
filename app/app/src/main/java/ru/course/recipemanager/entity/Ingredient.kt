package ru.course.recipemanager.entity

data class Ingredient(
    val name: String,
    val amount: String,
    val unit: String
) {
    fun displayName(): String {
        val quantity = listOf(amount, unit).filter { it.isNotBlank() }.joinToString(" ")
        return if (quantity.isBlank()) name else "$name — $quantity"
    }
}

data class CookingStep(
    val number: Int,
    val text: String
)
