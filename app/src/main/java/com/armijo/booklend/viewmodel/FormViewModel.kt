package com.armijo.booklend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armijo.booklend.model.LoanRequest
import com.armijo.booklend.repository.BookRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

sealed class FormState {
    object Idle : FormState()
    object Loading : FormState()
    data class Success(val request: LoanRequest) : FormState()
    data class Error(val message: String) : FormState()
    data class ValidationError(val errors: Map<String, String>) : FormState()
}

class FormViewModel : ViewModel() {

    private val repository = BookRepository()

    private val _formState = MutableLiveData<FormState>(FormState.Idle)
    val formState: LiveData<FormState> = _formState

    fun submitLoan(
        bookId: Int,
        bookTitle: String,
        userName: String,
        userEmail: String,
        userPhone: String,
        returnDate: String,
        notes: String
    ) {
        val errors = validateForm(userName, userEmail, userPhone, returnDate)
        if (errors.isNotEmpty()) {
            _formState.value = FormState.ValidationError(errors)
            return
        }

        viewModelScope.launch {
            _formState.value = FormState.Loading
            try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val request = LoanRequest(
                    bookId = bookId,
                    bookTitle = bookTitle,
                    userName = userName.trim(),
                    userEmail = userEmail.trim().lowercase(),
                    userPhone = userPhone.trim(),
                    requestDate = sdf.format(Date()),
                    returnDate = returnDate,
                    notes = notes.trim()
                )
                val result = repository.submitLoanRequest(request)
                result.fold(
                    onSuccess = { _formState.value = FormState.Success(it) },
                    onFailure = { _formState.value = FormState.Error(it.message ?: "Error desconocido") }
                )
            } catch (e: Exception) {
                _formState.value = FormState.Error("Error al procesar solicitud: ${e.message}")
            }
        }
    }

    private fun validateForm(
        name: String, email: String, phone: String, returnDate: String
    ): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (name.isBlank()) errors["name"] = "El nombre es requerido"
        else if (name.trim().length < 3) errors["name"] = "El nombre debe tener al menos 3 caracteres"

        if (email.isBlank()) errors["email"] = "El correo es requerido"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            errors["email"] = "Formato de correo inválido"

        if (phone.isBlank()) errors["phone"] = "El teléfono es requerido"
        else if (phone.replace("[^0-9]".toRegex(), "").length < 8)
            errors["phone"] = "Teléfono inválido (mínimo 8 dígitos)"

        if (returnDate.isBlank()) errors["returnDate"] = "La fecha de devolución es requerida"

        return errors
    }

    fun resetForm() {
        _formState.value = FormState.Idle
    }
}
