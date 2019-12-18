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

package com.eleven.ctruong.w2eat.repositories.models

import androidx.room.*

/**
 * User model
 *
 * @author el_even
 * @version 1.0
 * @since 2019, Nov 27th
 */
@Entity
data class User(
    @PrimaryKey
    var id: Long? = 0,
    var email: String? = "",
    var password: String? = ""
)

@Entity
data class Review(
    @PrimaryKey
    @ColumnInfo(name = "review_id")
    var id: Long? = 0,
    var author: Long? = 0,
    var recipe: Long? = 0,
    var note: Int? = 0,
    var comment: String? = "",
    var date: String? = ""
)

@Entity
data class Meal(
    @PrimaryKey
    @ColumnInfo(name = "meal_id")
    var id: Long? = 0,
    var name: String? = "",
    var owner: Long? = 0,
    @Ignore
    var photosUrl: MutableList<String>? = mutableListOf(),
    @Ignore
    var category: Category? = Category.FRENCH
)

enum class Category {
    ASIAN, FRENCH, ITALIAN
}

enum class Language {
    EN, FR, VN
}

@Entity
data class Recipe(
    @PrimaryKey @ColumnInfo(name = "recipe_id") var id: Long? = 0,
    var author: Long? = 0,
    @ColumnInfo(name = "recipe_meal_id")
    var meal: Long? = 0,
    @Ignore
    var languageSupport: Language? = Language.EN,
    @ColumnInfo(name = "note_average") var note: Float? = 0.0f
)

@Entity
data class Step(
    @PrimaryKey @ColumnInfo(name="step_id") var id: Long? = 0,
    @ColumnInfo(name = "step_recipe_id") var recipe: Long? = 0,
    @ColumnInfo(name="step_number") var number: Int? = 0,
    var name: String? = "",
    var description: String? = ""
)

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = 0,
    @ColumnInfo(name = "user_id")
    var user: Long? = 0,
    @ColumnInfo(name="meal_id")
    var meal: Long? = 0
)

data class UserWithFavorites(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val favorite: Favorite
)

data class MealAndRecipe(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "meal_id",
        entityColumn = "recipe_meal_id"
    )
    val recipe: Recipe
)


data class RecipeWithSteps(
    @Embedded
    val recipe: Recipe,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "step_recipe_id"
    )
    val steps: MutableList<Step>? = mutableListOf()
)

data class RecipeAndReviews(
    @Embedded
    val recipe: Recipe,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "recipe"
    )
    val reviews: MutableList<Review>? = mutableListOf()
)

data class UserWithOwnMeals(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "owner"
    )
    val meals: MutableList<Meal>? = mutableListOf()
)

data class UserAndReviews(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "author"
    )
    val reviews: MutableList<Review>? = mutableListOf()
)