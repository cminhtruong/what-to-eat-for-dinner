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
    @PrimaryKey var email: String? = null,
    var password: String? = "",
    @Embedded
    var favorite: Favorite? = Favorite()
)

@Entity
data class Review(
    @PrimaryKey
    var reviewId: String? = "",
    var author: String? = "",
    var recipe: String? = "",
    var note: Int? = 0,
    var comment: String? = "",
    var date: String? = ""
)

@Entity
data class Meal(
    @PrimaryKey
    var mealId: String? = "",
    var name: String? = "",
    var owner: String? = "",
    @Ignore
    var photosUrl: MutableList<String>? = mutableListOf(),
    var category: Category? = Category.FRENCH,
    @Ignore
    var reviews: MutableList<Review>? = mutableListOf()
)

enum class Category {
    ASIAN, FRENCH, ITALIAN
}

enum class Language {
    EN, FR, VN
}

@Entity
data class Recipe(
    @PrimaryKey var recipeId: String? = "",
    var author: String? = "",
    var recipeMealId: String? = "",
    @ColumnInfo(name = "language")
    var languageSupport: Language? = Language.EN,
    @ColumnInfo(name = "note")
    var noteAverage: Float? = 0.0f
)

@Entity
data class Step(
    @PrimaryKey var stepId: String? = "",
    var stepRecipeId: String? = "",
    var stepNumber: Int? = 0,
    var name: String? = "",
    var description: String? = ""
)

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 0,
    val meals: MutableList<Meal>? = mutableListOf()
)

data class MealAndRecipe(
    @Embedded var meal: Meal? = Meal(),
    @Relation(
        parentColumn = "mealId",
        entityColumn = "recipeMealId"
    )
    var recipe: Recipe? = Recipe()
)


data class RecipeWithSteps(
    @Embedded
    var recipe: Recipe? = Recipe(),
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "stepRecipeId"
    )
    var steps: MutableList<Step>? = mutableListOf()
)

data class RecipeAndReviews(
    @Embedded
    var recipe: Recipe? = Recipe(),
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipe"
    )
    var reviews: MutableList<Review>? = mutableListOf()
)

data class UserWithOwnMeals(
    @Embedded var user: User? = User(),
    @Relation(
        parentColumn = "email",
        entityColumn = "owner"
    )
    var meals: MutableList<Meal>? = mutableListOf()
)

data class UserAndReviews(
    @Embedded var user: User? = User(),
    @Relation(
        parentColumn = "email",
        entityColumn = "author"
    )
    var reviews: MutableList<Review>? = mutableListOf()
)