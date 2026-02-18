package com.armijo.booklend.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.armijo.booklend.model.Book
import com.armijo.booklend.databinding.ItemBookBinding

class BookAdapter(private val onItemClick: (Book) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var books = listOf<Book>()

    fun submitList(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size

    inner class BookViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.tvTitle.text = book.title
            binding.tvAuthor.text = book.author
            binding.tvAvailability.text = if (book.isAvailable) "Disponible" else "Prestado"
            binding.tvAvailability.setTextColor(if (book.isAvailable) android.graphics.Color.GREEN else android.graphics.Color.RED)

            // EventListener
            binding.root.setOnClickListener { onItemClick(book) }
        }
    }
}