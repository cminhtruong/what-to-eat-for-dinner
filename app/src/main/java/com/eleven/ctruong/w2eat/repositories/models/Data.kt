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
    val password: String? = ""
)

@Entity
data class Review(
    @PrimaryKey
    val reviewId: String,
    val author: String,
    val recipe: String? = "",
    val note: Int? = 0,
    val comment: String? = "",
    val date: String? = ""
)

@Entity
data class Meal(
    @PrimaryKey
    val mealId: String,
    val name: String? = "",
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
    @PrimaryKey val recipeId: String,
    val author: String,
    val recipeMealId: String,
    @Ignore
    val languageSupport: Language? = Language.EN,
    @ColumnInfo(name = "note")
    val noteAverage: Float? = 0.0f
)

@Entity
data class Step(
    @PrimaryKey val stepId: String,
    val stepRecipeId: String,
    val stepNumber: Int? = 0,
    val name: String? = "",
    val description: String? = ""
)

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 0,
    val userId: String? = "",
    val meals: MutableList<Meal>
)

data class UserWithFavorites(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "email",
        entityColumn = "userId"
    )
    val favorite: Favorite
)

data class MealAndRecipe(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "recipeMealId"
    )
    val recipe: Recipe
)


data class RecipeWithSteps(
    @Embedded
    val recipe: Recipe,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "stepRecipeId"
    )
    val steps: MutableList<Step>? = mutableListOf()
)

data class RecipeAndReviews(
    @Embedded
    val recipe: Recipe,
    @Relation(
        parentColumn = "recipeId",
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