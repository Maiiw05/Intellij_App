package com.example.appiumint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appiumint.databinding.FragmentTransBinding
import com.example.appiumint.network.RegisterTransactionRequest
import com.example.appiumint.network.RegisterTransactionResponse
import com.example.appiumint.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context


class trans  : Fragment() {

    private lateinit var binding: FragmentTransBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransBinding.inflate(inflater, container, false)

        binding.contactButton.setOnClickListener {
            findNavController().navigate(R.id.action_trans_to_contactos)
        }

        binding.btnContinuar.setOnClickListener {
            val numeroCuenta = binding.numeroCuenta.text.toString()
            val importe = binding.importe.text.toString().toDoubleOrNull()

            if (!numeroCuenta.isNullOrEmpty() && importe != null && importe > 0) {
                registrarTransaccion(numeroCuenta, importe)
            } else {
                Toast.makeText(requireContext(), "Datos inválidos", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun registrarTransaccion(numeroCuenta: String, importe: Double) {
        val sharedPreferences = activity?.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences?.getInt("id_usuario", -1) ?: -1

        if (idUsuario != -1) {
            val request = RegisterTransactionRequest(idUsuario, numeroCuenta.toInt(), importe)

            RetrofitClient.instance.registerTransaction(request).enqueue(object : Callback<RegisterTransactionResponse> {
                override fun onResponse(call: Call<RegisterTransactionResponse>, response: Response<RegisterTransactionResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!
                        if (result.success) {
                            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error al registrar la transacción", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterTransactionResponse>, t: Throwable) {
                    Log.e("TransError", "Error de conexión: ${t.message}")
                    Toast.makeText(requireContext(), "Fallo en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Sesión no válida, por favor inicia sesión", Toast.LENGTH_SHORT).show()
        }
    }
}