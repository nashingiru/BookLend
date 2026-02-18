package com.armijo.booklend.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.armijo.booklend.R
import com.armijo.booklend.databinding.FragmentHomeBinding
import com.armijo.booklend.ui.adapter.BookAdapter
import com.armijo.booklend.ui.viewmodel.BookViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BookViewModel
    private lateinit var adapter: BookAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Lifecycle", "HomeFragment: onViewCreated")

        viewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]

        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = BookAdapter { book ->
            // Navegación con Bundle
            val bundle = Bundle()
            bundle.putInt("book_id", book.id)
            val detailsFragment = DetailsFragment()
            detailsFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.rvBooks.layoutManager = LinearLayoutManager(context)
        binding.rvBooks.adapter = adapter
    }

    private fun observeData() {
        // Observación LiveData (Requisito MVVM)
        viewModel.books.observe(viewLifecycleOwner) { books ->
            adapter.submitList(books)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "HomeFragment: onStart")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
