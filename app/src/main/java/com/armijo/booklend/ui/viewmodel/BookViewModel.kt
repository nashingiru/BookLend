package com.armijo.booklend.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armijo.booklend.data.model.Book
import com.armijo.booklend.data.repository.BookRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay

class BookViewModel : ViewModel() {
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book> = _selectedBook

    init {
        loadBooks()
    }

    // Uso de Coroutines para simular carga asíncrona
    fun loadBooks() {
        viewModelScope.launch {
            // Simulamos delay de red
            delay(1000)
            _books.value = BookRepository.getAllBooks()
        }
    }

    fun selectBook(book: Book) {
        _selectedBook.value = book
    }
}