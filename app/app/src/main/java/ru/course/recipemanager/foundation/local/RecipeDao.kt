package ru.course.recipemanager.foundation.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe_cache ORDER BY title")
    fun observeRecipes(): Flow<List<RecipeCacheEntity>>

    @Query("SELECT * FROM recipe_cache WHERE id = :id LIMIT 1")
    suspend fun getRecipe(id: Long): RecipeCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecipes(recipes: List<RecipeCacheEntity>)

    @Query("SELECT * FROM shopping_items ORDER BY checked, name")
    fun observeShoppingItems(): Flow<List<ShoppingCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShoppingItems(items: List<ShoppingCacheEntity>)
}

