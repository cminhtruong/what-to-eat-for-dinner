package com.eleven.ctruong.w2eat.repositories.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eleven.ctruong.w2eat.repositories.models.*

@Dao
interface AppDatabaseDao {

    @Insert
    fun insertMeal(meal: Meal)

    @Insert
    fun insertReview(review: Review)

    @Insert
    fun insertRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRecipe(recipe: Recipe)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavorite(favorite: Favorite)

    @Insert
    fun insertFavorite(favorite: Favorite)

    @Insert
    fun insertUserOwnMeals(user: User, meals: MutableList<Meal>)

    @Transaction
    @Query("SELECT * from User")
    fun getFavorite(): LiveData<UserWithFavorites>

    @Transaction
    @Query("SELECT * FROM User")
    fun getMeals(): LiveData<MutableList<UserWithOwnMeals>>

    @Transaction
    @Query("SELECT * FROM Recipe")
    fun getRecipeWithSteps(): LiveData<MutableList<RecipeWithSteps>>

    @Transaction
    @Query("SELECT * FROM User")
    fun getReviews(): LiveData<MutableList<UserAndReviews>>

    @Transaction
    @Query("SELECT * FROM Meal")
    fun getMealWithRecipe(): LiveData<MealAndRecipe>

    @Transaction
    @Query("SELECT * FROM Recipe")
    fun getRecipeWithReview(): LiveData<MutableList<RecipeAndReviews>>
}