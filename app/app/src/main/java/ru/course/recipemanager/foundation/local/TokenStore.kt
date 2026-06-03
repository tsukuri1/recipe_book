package ru.course.recipemanager.foundation.local

import android.content.Context

class TokenStore(context: Context) {
    private val prefs = context.getSharedPreferences("ladushki_tokens", Context.MODE_PRIVATE)

    fun getAccessToken(): String? =
        prefs.getString(KEY_ACCESS_TOKEN, null)

    fun saveAccessToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun clear() {
        prefs.edit().remove(KEY_ACCESS_TOKEN).apply()
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
    }
}
