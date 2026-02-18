package com.armijo.booklend.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.armijo.booklend.R
import com.armijo.booklend.adapter.BookAdapter
import com.armijo.booklend.databinding.ActivityHomeBinding
import com.armijo.booklend.model.Book
import com.armijo.booklend.ui.detail.DetailActivity
import com.armijo.booklend.ui.form.FormActivity
import com.armijo.booklend.viewmodel.BooksUiState
import com.armijo.booklend.viewmodel.HomeViewModel
import com.google.android.material.chip.Chip

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HomeActivity"
    }

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter

    // ── Lifecycle ──────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupGenreChips()
        setupFab()
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    // ── Setup ──────────────────────────────────────────────────
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(
            onBookClick = { book -> openDetail(book) },
            onLoanClick = { book -> openForm(book) }
        )
        binding.rvBooks.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
        }
    }

    private fun setupGenreChips() {
        val genres = viewModel.genres
        genres.forEach { genre ->
            val chip = Chip(this).apply {
                text = genre
                isCheckable = true
                isChecked = genre == "Todos"
                setChipBackgroundColorResource(R.color.chip_background_selector)
                setTextColor(getColorStateList(R.color.chip_text_selector))
                chipStrokeWidth = 2f
            }
            chip.setOnClickListener {
                // Uncheck siblings
                for (i in 0 until binding.chipGroupGenres.childCount) {
                    (binding.chipGroupGenres.getChildAt(i) as? Chip)?.isChecked = false
                }
                chip.isChecked = true
                viewModel.filterByGenre(genre)
            }
            binding.chipGroupGenres.addView(chip)
        }
    }

    private fun setupFab() {
        binding.fabNewLoan.setOnClickListener {
            openForm(null)
        }
    }

    // ── Search Menu ────────────────────────────────────────────
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.apply {
            queryHint = "Buscar libros..."
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { viewModel.searchBooks(it) }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrBlank()) viewModel.loadBooks()
                    return true
                }
            })
        }
        return true
    }

    // ── Observe LiveData ───────────────────────────────────────
    private fun observeViewModel() {
        viewModel.booksState.observe(this) { state ->
            when (state) {
                is BooksUiState.Loading -> showLoading(true)
                is BooksUiState.Success -> {
                    showLoading(false)
                    showBooks(state.books)
                }
                is BooksUiState.Empty -> {
                    showLoading(false)
                    showEmpty(true)
                }
                is BooksUiState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ── UI State ───────────────────────────────────────────────
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvBooks.visibility = if (show) View.GONE else View.VISIBLE
        binding.layoutEmpty.visibility = View.GONE
    }

    private fun showBooks(books: List<Book>) {
        binding.layoutEmpty.visibility = View.GONE
        binding.rvBooks.visibility = View.VISIBLE
        bookAdapter.submitList(books)
        binding.tvBookCount.text = "${books.size} libro(s) encontrado(s)"
    }

    private fun showEmpty(show: Boolean) {
        binding.layoutEmpty.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvBooks.visibility = if (show) View.GONE else View.VISIBLE
    }

    // ── Navigation ─────────────────────────────────────────────
    private fun openDetail(book: Book) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_BOOK_ID, book.id)
            putExtra(DetailActivity.EXTRA_BOOK_TITLE, book.title)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    private fun openForm(book: Book?) {
        val intent = Intent(this, FormActivity::class.java).apply {
            book?.let {
                putExtra(FormActivity.EXTRA_BOOK_ID, it.id)
                putExtra(FormActivity.EXTRA_BOOK_TITLE, it.title)
            }
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
