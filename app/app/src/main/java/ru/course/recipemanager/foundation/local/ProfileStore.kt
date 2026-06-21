package ru.course.recipemanager.foundation.local

import android.content.Context
import ru.course.recipemanager.entity.UserProfile

class ProfileStore(context: Context) {
    private val preferences = context.getSharedPreferences("ladushki_profiles", Context.MODE_PRIVATE)

    fun load(email: String): StoredProfile? {
        val key = email.normalizedKey()
        val name = preferences.getString("$key:name", null)
        val avatarUri = preferences.getString("$key:avatar", null)
        return if (name == null && avatarUri == null) null else StoredProfile(name, avatarUri)
    }

    fun save(profile: UserProfile) {
        val key = profile.email.normalizedKey()
        preferences.edit()
            .putString("$key:name", profile.name)
            .putString("$key:avatar", profile.avatarUri)
            .apply()
    }

    private fun String.normalizedKey(): String = trim().lowercase()
}

data class StoredProfile(
    val name: String?,
    val avatarUri: String?
) {
    fun applyTo(profile: UserProfile): UserProfile = profile.copy(
        name = name ?: profile.name,
        avatarUri = avatarUri
    )
}
