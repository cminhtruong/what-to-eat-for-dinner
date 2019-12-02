package com.eleven.ctruong.w2eat.repositories.models

/**
 * Meal model
 *
 * @author el_even
 * @version 1.0
 * @since 2019, Nov 28th
 */
data class Meal(
    var id: String? = "",
    var photosUrl: MutableList<String>? = mutableListOf(),
    var name: String? = "",
    var category: Category? = Category.FRENCH,
    var recipe: Recipe? = null,
    var reviews: MutableList<Review>? = mutableListOf()
)

enum class Category {
    ASIAN, FRENCH, ITALIAN
}

enum class Language {
    EN, FR, VN
}

data class Recipe(
    var id: String? = "",
    var description: String? = "",
    var steps: MutableList<Step>? = mutableListOf(),
    var languageSupport: Language? = Language.EN,
    var noteAverage: Float? = 0.0f
)

data class Step(
    var stepNumber: Int? = 0,
    var name: String? = "",
    var description: String? = ""
)