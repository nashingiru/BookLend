package com.armijo.booklend.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.armijo.booklend.R
import com.armijo.booklend.databinding.FragmentDetailsBinding
import com.armijo.booklend.model.Book
import com.armijo.booklend.ui.viewmodel.BookViewModel

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BookViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Lifecycle", "DetailsFragment: onViewCreated")

        viewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]

        // Recuperar parámetro del Bundle (Requisito Navegación)
        val bookId = arguments?.getInt("book_id")

        viewModel.selectedBook.observe(viewLifecycleOwner) { book ->
            if (book != null) {
                binding.tvTitleDetail.text = book.title
                binding.tvAuthorDetail.text = book.author
                binding.tvDescDetail.text = book.description
                binding.ivDetail.setImageResource(book.coverResId)

                // Uso de let (Requisito Kotlin)
                book.title.let {
                    binding.toolbar.title = it
                }
            }
        }

        // Buscar el libro específico si no está en LiveData seleccionado aún
        bookId?.let { id ->
            viewModel.loadBooks()
        }

        binding.btnRequest.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("book_id", bookId ?: 0)
            val formFragment = FormFragment()
            formFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, formFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "DetailsFragment: onStart")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
