package com.armijo.booklend.model

data class LoanRequest(
    val id: Int = 0,
    val bookId: Int,
    val bookTitle: String,
    val userName: String,
    val userEmail: String,
    val userPhone: String,
    val requestDate: String,
    val returnDate: String,
    val notes: String = ""
)
