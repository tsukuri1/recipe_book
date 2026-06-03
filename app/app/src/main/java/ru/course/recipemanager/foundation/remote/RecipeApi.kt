package ru.course.recipemanager.foundation.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("users/me")
    suspend fun getProfile(): UserProfileDto

    @GET("recipes")
    suspend fun getRecipes(): List<RecipeDto>

    @GET("recipes/{id}")
    suspend fun getRecipe(@Path("id") id: Long): RecipeDto

    @POST("recipes")
    suspend fun createRecipe(@Body request: CreateRecipeRequest): RecipeDto

    @PUT("recipes/{id}")
    suspend fun updateRecipe(
        @Path("id") id: Long,
        @Body request: CreateRecipeRequest
    ): RecipeDto

    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(@Path("id") id: Long)

    @GET("recipes/search")
    suspend fun searchRecipes(
        @Query("query") query: String?,
        @Query("category") category: String?
    ): List<RecipeDto>

    @POST("favorites/{recipeId}")
    suspend fun addFavorite(@Path("recipeId") recipeId: Long)

    @DELETE("favorites/{recipeId}")
    suspend fun removeFavorite(@Path("recipeId") recipeId: Long)

    @GET("shopping-list")
    suspend fun getShoppingList(): List<ShoppingItemDto>

    @POST("shopping-list/from-recipe/{recipeId}")
    suspend fun addRecipeToShoppingList(@Path("recipeId") recipeId: Long)

    @PATCH("shopping-list/items/{itemId}")
    suspend fun toggleShoppingItem(@Path("itemId") itemId: Long)

    @DELETE("shopping-list/completed")
    suspend fun clearCompletedShoppingItems()

    @DELETE("shopping-list")
    suspend fun clearShoppingList()

    @GET("settings")
    suspend fun getSettings(): UserSettingsDto

    @PUT("settings")
    suspend fun updateSettings(@Body request: UserSettingsDto): UserSettingsDto

    @GET("admin/stats")
    suspend fun getAdminStats(): AdminStatsDto

    @GET("admin/recipes/pending")
    suspend fun getPendingRecipes(): List<RecipeDto>

    @PATCH("admin/recipes/{recipeId}/approve")
    suspend fun approveRecipe(@Path("recipeId") recipeId: Long): RecipeDto

    @PATCH("admin/recipes/{recipeId}/reject")
    suspend fun rejectRecipe(@Path("recipeId") recipeId: Long): RecipeDto

    @GET("admin/complaints")
    suspend fun getComplaints(): List<ComplaintDto>
}
