package com.eleven.ctruong.w2eat.repositories.models

import androidx.annotation.NonNull
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
    @PrimaryKey @NonNull var email: String? = null,
    var password: String? = "",
    @Embedded
    var favorite: Favorite? = Favorite()
)

@Entity
data class Review(
    @PrimaryKey
    @NonNull
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
    @NonNull
    var mealId: String? = "",
    var name: String? = "",
    var owner: String? = "",
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
    @PrimaryKey @NonNull var recipeId: String? = "",
    var author: String? = "",
    var recipeMealId: String? = "",
    @Ignore
    var languageSupport: Language? = Language.EN,
    @ColumnInfo(name = "note")
    var noteAverage: Float? = 0.0f
)

@Entity
data class Step(
    @PrimaryKey @NonNull var stepId: String? = "",
    var stepRecipeId: String? = "",
    var stepNumber: Int? = 0,
    var name: String? = "",
    var description: String? = ""
)

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 0,
    @Embedded
    val meals: Meal? = Meal()
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