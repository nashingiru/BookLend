package com.armijo.booklend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armijo.booklend.model.Book
import com.armijo.booklend.repository.BookRepository
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val repository = BookRepository()

    private val _book = MutableLiveData<Book?>()
    val book: LiveData<Book?> = _book

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadBook(bookId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getBookById(bookId)
            _book.value = result
            _isLoading.value = false
        }
    }

    fun getAvailabilityText(book: Book): String {
        return when {
            book.availableCopies == 0 -> "No disponible"
            book.availableCopies == 1 -> "1 ejemplar disponible"
            else -> "${book.availableCopies} ejemplares disponibles"
        }
    }
}
