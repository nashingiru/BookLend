package com.armijo.booklend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.armijo.booklend.R
import com.armijo.booklend.databinding.ItemBookBinding
import com.armijo.booklend.model.Book

class BookAdapter(
    private val onBookClick: (Book) -> Unit,
    private val onLoanClick: (Book) -> Unit
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.apply {
                tvTitle.text = book.title
                tvAuthor.text = book.author
                tvGenre.text = book.genre
                tvYear.text = book.year.toString()

                // Availability badge
                if (book.isAvailable) {
                    tvAvailability.text = "${book.availableCopies} disponible(s)"
                    tvAvailability.setTextColor(
                        ContextCompat.getColor(root.context, R.color.available_color)
                    )
                    tvAvailability.setBackgroundResource(R.drawable.bg_badge_available)
                    btnLoan.isEnabled = true
                    btnLoan.text = "Pedir préstamo"
                } else {
                    tvAvailability.text = "No disponible"
                    tvAvailability.setTextColor(
                        ContextCompat.getColor(root.context, R.color.unavailable_color)
                    )
                    tvAvailability.setBackgroundResource(R.drawable.bg_badge_unavailable)
                    btnLoan.isEnabled = false
                    btnLoan.text = "Sin ejemplares"
                }

                root.setOnClickListener { onBookClick(book) }
                btnLoan.setOnClickListener { onLoanClick(book) }
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Book, newItem: Book) = oldItem == newItem
    }
}
