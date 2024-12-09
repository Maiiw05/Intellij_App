package com.example.appiumint

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appiumint.adapters.MovementsAdapter
import com.example.appiumint.databinding.ActivityMiCuentaBinding
import com.example.appiumint.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Mi_cuenta : AppCompatActivity() {

    private lateinit var binding: ActivityMiCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("id_usuario", -1)
        val rol = sharedPreferences.getString("rol", null)

        // Mensajes de depuración para verificar los valores recuperados
        Log.d("MiCuentaDebug", "idUsuario recuperado: $idUsuario, rol recuperado: $rol")

        if (idUsuario == -1 || rol.isNullOrEmpty()) {
            Log.e("MiCuentaDebug", "Sesión no válida. idUsuario: $idUsuario, rol: $rol")
            Toast.makeText(this, "Sesión no válida, por favor inicia sesión de nuevo", Toast.LENGTH_LONG).show()
            navigateToInicioSesion()
            return
        }

        // Inicializar RecyclerView
        binding.notificationsList.layoutManager = LinearLayoutManager(this)

        // Llamar a los métodos para obtener los datos del usuario y movimientos
        fetchUserData(idUsuario)
        fetchMovements(idUsuario)
    }

    private fun navigateToInicioSesion() {
        val intent = Intent(this, inicio_sesion::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetchUserData(idUsuario: Int) {
        val request = GetUserDataRequest(idUsuario)

        RetrofitClient.instance.getUserData(request).enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                if (response.isSuccessful) {
                    val userData = response.body()?.data
                    if (userData != null) {
                        Log.d("MiCuentaDebug", "Datos de usuario obtenidos: ${userData.nombre} ${userData.apellido}")
                        binding.cardHolderName.text = "${userData.nombre} ${userData.apellido}"
                        binding.cardNumber.text = userData.email
                        binding.balanceAmount.text = "Teléfono: ${userData.telefono}"
                    } else {
                        Log.e("MiCuentaDebug", "Datos de usuario no encontrados")
                        Toast.makeText(this@Mi_cuenta, "Datos de usuario no encontrados", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MiCuentaDebug", "Error al obtener datos del usuario. Código: ${response.code()}")
                    Toast.makeText(this@Mi_cuenta, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                Log.e("MiCuentaDebug", "Error de conexión: ${t.message}")
                Toast.makeText(this@Mi_cuenta, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchMovements(idUsuario: Int) {
        RetrofitClient.instance.getMovements(mapOf("id_usuario" to idUsuario))
            .enqueue(object : Callback<MovementsResponse> {
                override fun onResponse(call: Call<MovementsResponse>, response: Response<MovementsResponse>) {
                    if (response.isSuccessful) {
                        val movements = response.body()?.data
                        if (movements != null) {
                            Log.d("MiCuentaDebug", "Movimientos obtenidos: ${movements.size}")
                            val adapter = MovementsAdapter(movements)
                            binding.notificationsList.adapter = adapter
                        } else {
                            Log.e("MiCuentaDebug", "No se encontraron movimientos")
                            Toast.makeText(this@Mi_cuenta, "No se encontraron movimientos", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("MiCuentaDebug", "Error al obtener movimientos. Código: ${response.code()}")
                        Toast.makeText(this@Mi_cuenta, "Error al obtener movimientos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MovementsResponse>, t: Throwable) {
                    Log.e("MiCuentaDebug", "Error de conexión: ${t.message}")
                    Toast.makeText(this@Mi_cuenta, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
