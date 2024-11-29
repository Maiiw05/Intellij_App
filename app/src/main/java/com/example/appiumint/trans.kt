package com.example.appiumint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.appiumint.databinding.FragmentTransBinding
import com.example.appiumint.viewmodel.SharedViewModel


class trans : Fragment() {

    private lateinit var binding: FragmentTransBinding
    private val sharedViewModel: SharedViewModel by activityViewModels() // ViewModel compartido

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransBinding.inflate(inflater, container, false)

        // Observar los cambios en el número de cuenta seleccionado
        sharedViewModel.selectedAccount.observe(viewLifecycleOwner) { accountNumber ->
            binding.numeroCuenta.setText(accountNumber)
            // Reflejar el número de cuenta
        }

        return binding.root
    }
}
