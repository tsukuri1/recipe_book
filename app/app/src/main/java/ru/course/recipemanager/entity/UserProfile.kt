package ru.course.recipemanager.entity

data class UserProfile(
    val name: String,
    val email: String,
    val role: UserRole,
    val recipesCount: Int,
    val favoriteCount: Int,
    val offlineCount: Int,
    val avatarUri: String? = null
) {
    val isAdmin: Boolean = role == UserRole.Admin
}

enum class UserRole(val label: String) {
    User("USER"),
    Admin("ADMIN")
}

data class UserSettings(
    val syncEnabled: Boolean = true,
    val offlineCacheEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val compactModeEnabled: Boolean = false
)

data class AdminStats(
    val usersCount: Int,
    val recipesCount: Int,
    val pendingRecipesCount: Int,
    val reportsCount: Int
)
