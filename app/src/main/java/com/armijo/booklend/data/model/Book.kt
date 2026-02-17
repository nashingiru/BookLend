package com.armijo.booklend.data.model

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val coverUrl: String, // URL o recurso local -> Necesito picasso si es url
    val isAvailable: Boolean
)