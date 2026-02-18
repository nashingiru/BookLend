package com.armijo.booklend.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armijo.booklend.model.Book
import com.armijo.booklend.repository.BookRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    private val _selectedBook = MutableLiveData<Book?>()
    val selectedBook: LiveData<Book?> = _selectedBook

    private val repository = BookRepository()

    init {
        loadBooks()
    }

    // Uso de Coroutines (Requisito Asíncrono)
    fun loadBooks() {
        viewModelScope.launch {
            delay(1000) // Simular carga de red
            _books.value = repository.getAllBooks()
        }
    }

    fun selectBook(book: Book) {
        _selectedBook.value = book
    }

    fun clearSelection() {
        _selectedBook.value = null
    }
}