package com.example.appiumint

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appiumint.network.LoginRequest
import com.example.appiumint.network.LoginResponse
import com.example.appiumint.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class inicio_sesion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_sesion)

        val usernameInput: EditText = findViewById(R.id.username)
        val passwordInput: EditText = findViewById(R.id.password)
        val signInButton: Button = findViewById(R.id.sign_in_button)

        signInButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                login(username, password)
            } else {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(username: String, password: String) {
        val request = LoginRequest(username, password)

        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("LoginResponse", "Respuesta del servidor: $result")

                    if (result != null && result.success) {
                        // Agregamos registros para verificar los valores
                        Log.d("LoginDebug", "id_usuario del servidor: ${result.id_usuario}, rol del servidor: ${result.rol}")

                        if (result.id_usuario != null && result.rol != null) {
                            val success = saveSessionData(result.id_usuario, result.rol)
                            if (success) {
                                Toast.makeText(this@inicio_sesion, "Bienvenido, ${result.rol}", Toast.LENGTH_SHORT).show()
                                navigateToMiCuenta()
                            } else {
                                Toast.makeText(
                                    this@inicio_sesion,
                                    "Error guardando datos de sesión",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@inicio_sesion,
                                "Datos del usuario incompletos, por favor intenta de nuevo",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@inicio_sesion,
                            result?.message ?: "Error al iniciar sesión",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this@inicio_sesion, "Error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@inicio_sesion, "Fallo en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveSessionData(idUsuario: Int, rol: String): Boolean {
        val sharedPreferences = this.getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val success = editor.putInt("id_usuario", idUsuario)
            .putString("rol", rol)
            .commit()  // Devuelve true si se guardó correctamente

        // Verificamos que los datos se hayan guardado correctamente
        val savedIdUsuario = sharedPreferences.getInt("id_usuario", -1)
        val savedRol = sharedPreferences.getString("rol", null)
        Log.d("SaveSessionDataDebug", "id_usuario guardado: $savedIdUsuario, rol guardado: $savedRol")

        return success
    }

    private fun navigateToMiCuenta() {
        val intent = Intent(this@inicio_sesion, Mi_cuenta::class.java)
        startActivity(intent)
        finish()
    }
}
