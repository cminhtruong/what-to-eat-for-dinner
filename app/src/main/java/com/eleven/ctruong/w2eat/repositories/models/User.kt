package com.eleven.ctruong.w2eat.repositories.models

/**
 * User model
 *
 * @author el_even
 * @version 1.0
 * @since 2019, Nov 27th
 */
data class User(
    var email: String? = "",
    var password: String? = "",
    var meal: MutableList<Meal>? = mutableListOf(),
    var favoriteMeals: MutableList<Meal>? = mutableListOf(),
    var reviews: MutableList<Review>? = mutableListOf()
)

data class Review(
    var id: String?= "",
    var author: User? = User(),
    var meal: Meal? = Meal(),
    var note: Int? = 0,
    var comment: String? = "",
    var date: String? = ""
)