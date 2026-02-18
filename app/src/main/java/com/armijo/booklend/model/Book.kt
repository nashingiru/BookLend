package com.armijo.booklend.model

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val genre: String,
    val year: Int,
    val synopsis: String,
    val totalCopies: Int,
    val availableCopies: Int,
    val isAvailable: Boolean = availableCopies > 0,
    val description: String = synopsis, // For compatibility
    val coverResId: Int = 0 // Placeholder
)
