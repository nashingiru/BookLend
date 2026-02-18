package com.armijo.booklend.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.armijo.booklend.databinding.FragmentFormBinding
import com.google.android.material.textfield.TextInputEditText

class FormFragment : Fragment() {
    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Uso de apply (Requisito Kotlin)
        binding.btnSend.apply {
            setOnClickListener {
                validateAndSend()
            }
        }
    }

    private fun validateAndSend() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context, "Solicitud enviada para $name", Toast.LENGTH_LONG).show()

        // Volver al inicio
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}