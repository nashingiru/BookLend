package com.armijo.booklend.data.repository

import com.armijo.booklend.data.model.Book

object BookRepository {
    private val books = listOf(
        Book(1, "El fin de la muerte", "Liu", "", true),
        Book(2, "Babel", "Kuang", "", false),
        Book(3, "Harry Potter", "Rowling", "", true)
    )

    fun getAllBooks(): List<Book> = books
    fun getAvailableBooks(): List<Book> = books.filter { it.isAvailable }
    fun getBookById(id: Int): Book? = books.find { it.id == id }
}