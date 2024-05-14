package com.facebook.labourhub

import java.io.Serializable

data class Post(
    val username: String,
    val age: String,
    val mobile: String,
    val area: String,
    val category: String,
    val adhaar: String,
    val image_url: String
): Serializable
