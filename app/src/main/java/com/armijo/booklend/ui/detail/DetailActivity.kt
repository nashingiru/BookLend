package com.armijo.booklend.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.armijo.booklend.R
import com.armijo.booklend.databinding.ActivityDetailBinding
import com.armijo.booklend.model.Book
import com.armijo.booklend.ui.form.FormActivity
import com.armijo.booklend.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BOOK_ID = "extra_book_id"
        const val EXTRA_BOOK_TITLE = "extra_book_title"
        private const val TAG = "DetailActivity"
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var bookId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del Bundle (Intent)
        bookId = intent.getIntExtra(EXTRA_BOOK_ID, -1)
        val bookTitle = intent.getStringExtra(EXTRA_BOOK_TITLE) ?: "Detalle"

        setupToolbar(bookTitle)
        observeViewModel()

        if (bookId != -1) {
            viewModel.loadBook(bookId)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            this.title = title
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.layoutContent.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewModel.book.observe(this) { book ->
            book?.let { displayBook(it) }
        }
    }

    private fun displayBook(book: Book) {
        with(binding) {
            tvTitle.text = book.title
            tvAuthor.text = book.author
            tvGenre.text = book.genre
            tvYear.text = book.year.toString()
            tvSynopsis.text = book.synopsis
            tvCopiesTotal.text = "Total: ${book.totalCopies} ejemplares"
            tvCopiesAvailable.text = viewModel.getAvailabilityText(book)

            // Availability status
            if (book.isAvailable) {
                tvAvailabilityStatus.text = "DISPONIBLE"
                tvAvailabilityStatus.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.available_color))
                cardAvailability.setCardBackgroundColor(ContextCompat.getColor(this@DetailActivity, R.color.available_bg))
                btnRequestLoan.isEnabled = true
                btnRequestLoan.text = "Solicitar Préstamo"
            } else {
                tvAvailabilityStatus.text = "NO DISPONIBLE"
                tvAvailabilityStatus.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.unavailable_color))
                cardAvailability.setCardBackgroundColor(ContextCompat.getColor(this@DetailActivity, R.color.unavailable_bg))
                btnRequestLoan.isEnabled = false
                btnRequestLoan.text = "Sin ejemplares disponibles"
            }

            btnRequestLoan.setOnClickListener {
                val intent = Intent(this@DetailActivity, FormActivity::class.java).apply {
                    putExtra(FormActivity.EXTRA_BOOK_ID, book.id)
                    putExtra(FormActivity.EXTRA_BOOK_TITLE, book.title)
                }
                startActivity(intent)
            }
        }
    }
}
