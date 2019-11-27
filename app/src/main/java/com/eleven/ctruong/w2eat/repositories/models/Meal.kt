package com.eleven.ctruong.w2eat.repositories.models

data class Meal(
    var id: String,
    var photosUrl: MutableList<String>,
    var name: String,
    var category: Category,
    var recipe: Recipe,
    var reviews: MutableList<Review>
)

enum class Category {
    ASIAN, FRENCH, ITALIAN
}

enum class Language {
    EN, FR, VN
}

data class Recipe(
    var id: String,
    var description: String,
    var steps: MutableList<Step>,
    var languageSupport: String
)

data class Step(
    var stepNumber: Int,
    var name: String,
    var description: String
)