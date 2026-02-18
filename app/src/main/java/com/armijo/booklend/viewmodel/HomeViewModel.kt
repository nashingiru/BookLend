package com.armijo.booklend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armijo.booklend.model.Book
import com.armijo.booklend.repository.BookRepository
import kotlinx.coroutines.launch

sealed class BooksUiState {
    object Loading : BooksUiState()
    data class Success(val books: List<Book>) : BooksUiState()
    data class Error(val message: String) : BooksUiState()
    object Empty : BooksUiState()
}

class HomeViewModel : ViewModel() {

    private val repository = BookRepository()

    private val _booksState = MutableLiveData<BooksUiState>()
    val booksState: LiveData<BooksUiState> = _booksState

    private val _selectedGenre = MutableLiveData<String>("Todos")
    val selectedGenre: LiveData<String> = _selectedGenre

    val genres: List<String> get() = listOf("Todos") + repository.getGenres()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _booksState.value = BooksUiState.Loading
            try {
                val books = repository.getAllBooks()
                _booksState.value = if (books.isEmpty()) BooksUiState.Empty
                                    else BooksUiState.Success(books)
            } catch (e: Exception) {
                _booksState.value = BooksUiState.Error("Error al cargar libros: ${e.message}")
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _booksState.value = BooksUiState.Loading
            try {
                val books = if (query.isBlank()) repository.getAllBooks()
                            else repository.searchBooks(query)
                _booksState.value = if (books.isEmpty()) BooksUiState.Empty
                                    else BooksUiState.Success(books)
            } catch (e: Exception) {
                _booksState.value = BooksUiState.Error("Error en búsqueda: ${e.message}")
            }
        }
    }

    fun filterByGenre(genre: String) {
        _selectedGenre.value = genre
        viewModelScope.launch {
            _booksState.value = BooksUiState.Loading
            try {
                val books = if (genre == "Todos") repository.getAllBooks()
                            else repository.getBooksByGenre(genre)
                _booksState.value = if (books.isEmpty()) BooksUiState.Empty
                                    else BooksUiState.Success(books)
            } catch (e: Exception) {
                _booksState.value = BooksUiState.Error("Error al filtrar: ${e.message}")
            }
        }
    }

    // Kotlin higher-order functions example
    fun getAvailableBooks(books: List<Book>): List<Book> {
        return books.filter { it.isAvailable }
    }

    fun getBooksStats(books: List<Book>): Map<String, Int> {
        return mapOf(
            "total" to books.size,
            "disponibles" to books.count { it.isAvailable },
            "prestados" to books.count { !it.isAvailable }
        )
    }
}
