package ru.course.recipemanager

import android.app.Application
import ru.course.recipemanager.foundation.CachedRecipeRepository
import ru.course.recipemanager.foundation.RecipeRepository
import ru.course.recipemanager.foundation.local.AppDatabase
import ru.course.recipemanager.foundation.local.TokenStore
import ru.course.recipemanager.foundation.remote.NetworkModule

class LadushkiApplication : Application() {
    lateinit var repository: RecipeRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val tokenStore = TokenStore(this)
        val api = NetworkModule.createRecipeApi(
            baseUrl = "http://10.0.2.2:8082/api/",
            tokenProvider = { tokenStore.getAccessToken() }
        )
        val dao = AppDatabase.create(this).recipeDao()

        repository = CachedRecipeRepository(api, dao, tokenStore)
    }
}
