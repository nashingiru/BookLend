package com.armijo.booklend.ui.form

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.armijo.booklend.databinding.ActivityFormBinding
import com.armijo.booklend.viewmodel.FormState
import com.armijo.booklend.viewmodel.FormViewModel
import java.text.SimpleDateFormat
import java.util.*

class FormActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BOOK_ID = "extra_book_id"
        const val EXTRA_BOOK_TITLE = "extra_book_title"
        private const val TAG = "FormActivity"
    }

    private lateinit var binding: ActivityFormBinding
    private val viewModel: FormViewModel by viewModels()

    private var bookId: Int = -1
    private var bookTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getIntExtra(EXTRA_BOOK_ID, -1)
        bookTitle = intent.getStringExtra(EXTRA_BOOK_TITLE) ?: ""

        setupToolbar()
        setupForm()
        observeViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { onBackPressedDispatcher.onBackPressed(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Solicitud de Préstamo"
        }
    }

    private fun setupForm() {
        // Pre-fill book title if coming from detail
        if (bookTitle.isNotEmpty()) {
            binding.etBookTitle.setText(bookTitle)
            binding.etBookTitle.isEnabled = false
        }

        // Date picker
        binding.etReturnDate.setOnClickListener { showDatePicker() }
        binding.tilReturnDate.setEndIconOnClickListener { showDatePicker() }

        // Submit
        binding.btnSubmit.setOnClickListener {
            submitForm()
        }

        // Clear form
        binding.btnClear.setOnClickListener {
            clearForm()
            viewModel.resetForm()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 7) // Default: one week from now

        DatePickerDialog(
            this,
            { _, year, month, day ->
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val cal = Calendar.getInstance().apply { set(year, month, day) }
                binding.etReturnDate.setText(sdf.format(cal.time))
                binding.tilReturnDate.error = null
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()
    }

    private fun submitForm() {
        val bookTitleVal = if (bookTitle.isNotEmpty()) bookTitle
                          else binding.etBookTitle.text.toString()

        viewModel.submitLoan(
            bookId = bookId,
            bookTitle = bookTitleVal,
            userName = binding.etName.text.toString(),
            userEmail = binding.etEmail.text.toString(),
            userPhone = binding.etPhone.text.toString(),
            returnDate = binding.etReturnDate.text.toString(),
            notes = binding.etNotes.text.toString()
        )
    }

    private fun observeViewModel() {
        viewModel.formState.observe(this) { state ->
            when (state) {
                is FormState.Idle -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSubmit.isEnabled = true
                }
                is FormState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSubmit.isEnabled = false
                }
                is FormState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSubmit.isEnabled = true
                    showSuccessDialog(state.request.bookTitle, state.request.returnDate)
                }
                is FormState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSubmit.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                is FormState.ValidationError -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSubmit.isEnabled = true
                    showValidationErrors(state.errors)
                }
            }
        }
    }

    private fun showValidationErrors(errors: Map<String, String>) {
        binding.tilName.error = errors["name"]
        binding.tilEmail.error = errors["email"]
        binding.tilPhone.error = errors["phone"]
        binding.tilReturnDate.error = errors["returnDate"]
    }

    private fun showSuccessDialog(bookTitle: String, returnDate: String) {
        AlertDialog.Builder(this)
            .setTitle("¡Solicitud enviada!")
            .setMessage("Tu solicitud para el libro \"$bookTitle\" ha sido registrada.\nFecha de devolución: $returnDate")
            .setPositiveButton("Aceptar") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun clearForm() {
        if (bookTitle.isEmpty()) binding.etBookTitle.text?.clear()
        binding.etName.text?.clear()
        binding.etEmail.text?.clear()
        binding.etPhone.text?.clear()
        binding.etReturnDate.text?.clear()
        binding.etNotes.text?.clear()
        listOf(binding.tilName, binding.tilEmail, binding.tilPhone, binding.tilReturnDate).forEach {
            it.error = null
        }
    }
}
