package com.example.appiumint

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appiumint.adapters.MovementsAdapter
import com.example.appiumint.databinding.ActivityMiCuentaBinding
import com.example.appiumint.network.MovementsResponse
import com.example.appiumint.network.RetrofitClient
import com.example.appiumint.network.UserDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Mi_cuenta : AppCompatActivity() {

    private lateinit var binding: ActivityMiCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración inicial del RecyclerView
        binding.notificationsList.layoutManager = LinearLayoutManager(this)

        // Obtener ID de usuario desde el Intent
        val idUsuario = intent.getIntExtra("id_usuario", -1)
        if (idUsuario != -1) {
            fetchUserData(idUsuario) // Llama al método aquí
            fetchMovements(idUsuario)
        } else {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show()
        }
    }
    private fun fetchMovements(idUsuario: Int) {
        val request = mapOf("id_usuario" to idUsuario)

        RetrofitClient.instance.getMovements(request).enqueue(object : Callback<MovementsResponse> {
            override fun onResponse(
                call: Call<MovementsResponse>,
                response: Response<MovementsResponse>
            ) {
                if (response.isSuccessful) {
                    val movements = response.body()?.data
                    if (movements != null) {
                        // Configurar el RecyclerView con el adaptador de movimientos
                        val adapter = MovementsAdapter(movements)
                        binding.notificationsList.adapter = adapter
                    } else {
                        Toast.makeText(this@Mi_cuenta, "No se encontraron movimientos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Mi_cuenta, "Error al obtener movimientos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovementsResponse>, t: Throwable) {
                Toast.makeText(this@Mi_cuenta, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchUserData(idUsuario: Int) {
        val request = mapOf("id_usuario" to idUsuario)

        RetrofitClient.instance.getUserData(request).enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(
                call: Call<UserDataResponse>,
                response: Response<UserDataResponse>
            ) {
                if (response.isSuccessful) {
                    val userData = response.body()?.data
                    if (userData != null) {
                        binding.cardHolderName.text = userData.nombre
                        binding.cardNumber.text = userData.numero_cuenta
                        binding.balanceAmount.text = "$${userData.saldo}"
                    } else {
                        Toast.makeText(this@Mi_cuenta, "Datos no encontrados", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Mi_cuenta, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                Toast.makeText(this@Mi_cuenta, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }}

