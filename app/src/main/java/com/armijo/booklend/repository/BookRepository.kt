package com.armijo.booklend.repository

import com.armijo.booklend.model.Book
import com.armijo.booklend.model.LoanRequest
import kotlinx.coroutines.delay

class BookRepository {

    // Simulated database of books
    private val books = mutableListOf(
        Book(1, "Cien Años de Soledad", "Gabriel García Márquez", "Novela", 1967,
            "La saga de la familia Buendía a lo largo de siete generaciones en el mítico pueblo de Macondo. Una obra maestra del realismo mágico latinoamericano que explora el tiempo, la soledad y el destino de toda una estirpe.", 5, 3),
        Book(2, "El Principito", "Antoine de Saint-Exupéry", "Fábula", 1943,
            "Un aviador que cae en el desierto del Sahara conoce a un pequeño príncipe que habita en un asteroide. A través de sus conversaciones, descubren las verdades esenciales de la existencia humana.", 8, 5),
        Book(3, "Don Quijote de la Mancha", "Miguel de Cervantes", "Clásico", 1605,
            "Las aventuras del hidalgo Alonso Quijano quien, enloquecido por la lectura de novelas de caballería, decide convertirse en caballero andante bajo el nombre de Don Quijote de la Mancha, acompañado de su fiel escudero Sancho Panza.", 4, 1),
        Book(4, "1984", "George Orwell", "Distopía", 1949,
            "En un mundo totalitario dominado por el omnipresente Gran Hermano, Winston Smith trabaja para el Partido reescribiendo la historia. Su rebelión secreta contra el sistema lo lleva a una relación prohibida y a una búsqueda desesperada de la verdad.", 6, 4),
        Book(5, "Crimen y Castigo", "Fiódor Dostoyevski", "Novela", 1866,
            "Rodión Raskólnikov, un joven estudiante de derecho en San Petersburgo, comete un crimen convencido de su superioridad moral. La novela explora la psicología del culpable y el peso del remordimiento en la conciencia humana.", 3, 0),
        Book(6, "El Alquimista", "Paulo Coelho", "Ficción", 1988,
            "Santiago, un joven pastor andaluz, emprende un viaje hacia las pirámides de Egipto en busca de un tesoro, guiado por señales y su propia intuición. Una parábola sobre el cumplimiento de los sueños personales y escuchar al corazón.", 7, 6),
        Book(7, "Orgullo y Prejuicio", "Jane Austen", "Romance", 1813,
            "Elizabeth Bennet y el señor Darcy protagonizan una historia de amor que debe superar los prejuicios sociales y el orgullo personal en la Inglaterra del siglo XIX. Una aguda crítica de la sociedad de la época.", 5, 2),
        Book(8, "El Señor de los Anillos", "J.R.R. Tolkien", "Fantasía", 1954,
            "La épica historia de Frodo Bolsón y la Comunidad del Anillo en su misión de destruir el Anillo Único y salvar la Tierra Media del oscuro señor Sauron. Una obra fundacional de la fantasía moderna.", 4, 4),
        Book(9, "Sapiens", "Yuval Noah Harari", "No Ficción", 2011,
            "Un recorrido por la historia de la humanidad desde los primeros homínidos hasta la era moderna, examinando cómo el Homo sapiens llegó a dominar el planeta a través de la evolución cognitiva, la agricultura y las estructuras sociales.", 6, 3),
        Book(10, "Fundación", "Isaac Asimov", "Ciencia Ficción", 1951,
            "Hari Seldon desarrolla la psicohistoria, una ciencia que predice el futuro de la humanidad, y establece la Fundación para preservar el conocimiento y acortar la era de barbarie que se avecina tras la caída del Imperio Galáctico.", 5, 5)
    )

    private val loanRequests = mutableListOf<LoanRequest>()
    private var nextLoanId = 1

    // Simulate network/DB delay
    suspend fun getAllBooks(): List<Book> {
        delay(800)
        return books.toList()
    }

    suspend fun searchBooks(query: String): List<Book> {
        delay(300)
        val q = query.lowercase()
        return books.filter {
            it.title.lowercase().contains(q) ||
            it.author.lowercase().contains(q) ||
            it.genre.lowercase().contains(q)
        }
    }

    suspend fun getBookById(id: Int): Book? {
        delay(200)
        return books.find { it.id == id }
    }

    suspend fun submitLoanRequest(request: LoanRequest): Result<LoanRequest> {
        delay(1000)
        return try {
            val saved = request.copy(id = nextLoanId++)
            loanRequests.add(saved)
            // Decrease available copies
            val bookIndex = books.indexOfFirst { it.id == request.bookId }
            if (bookIndex >= 0) {
                val book = books[bookIndex]
                books[bookIndex] = book.copy(availableCopies = (book.availableCopies - 1).coerceAtLeast(0))
            }
            Result.success(saved)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBooksByGenre(genre: String): List<Book> {
        delay(400)
        return books.filter { it.genre.equals(genre, ignoreCase = true) }
    }

    fun getGenres(): List<String> {
        return books.map { it.genre }.distinct().sorted()
    }
}
