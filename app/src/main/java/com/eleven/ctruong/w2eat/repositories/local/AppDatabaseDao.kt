/*
 * Copyright 2019, El_even
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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