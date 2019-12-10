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
    @PrimaryKey val email: String,
    val password: String
)

@Entity
data class Review(
    @PrimaryKey
    @ColumnInfo(name = "review_id")
    val id: String,
    val author: String,
    val recipe: String,
    val note: Int,
    val comment: String,
    val date: String
)

@Entity
data class Meal(
    @PrimaryKey
    @ColumnInfo(name = "meal_id")
    val id: String,
    val name: String,
    val owner: String,
    @Ignore
    val photosUrl: MutableList<String>? = mutableListOf(),
    @Ignore
    val category: Category? = Category.FRENCH
)

enum class Category {
    ASIAN, FRENCH, ITALIAN
}

enum class Language {
    EN, FR, VN
}

@Entity
data class Recipe(
    @PrimaryKey @ColumnInfo(name = "recipe_id") val id: String,
    val author: String,
    @ColumnInfo(name = "recipe_meal_id")
    val meal: String,
    @Ignore
    val languageSupport: Language? = Language.EN,
    @ColumnInfo(name = "note_average") val note: Float
)

@Entity
data class Step(
    @PrimaryKey @ColumnInfo(name="step_id") val id: String,
    @ColumnInfo(name = "step_recipe_id") val recipe: String,
    @ColumnInfo(name="step_number") val number: Int,
    val name: String,
    val description: String
)

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "user_id")
    val user: String,
    @Embedded
    val meals: Meal
)

data class UserWithFavorites(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "email",
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
        parentColumn = "email",
        entityColumn = "owner"
    )
    val meals: MutableList<Meal>? = mutableListOf()
)

data class UserAndReviews(
    @Embedded val user: User,
    @Relation(
        parentColumn = "email",
        entityColumn = "author"
    )
    val reviews: MutableList<Review>? = mutableListOf()
)