package com.example.appiumint
import com.example.appiumint.network.LoginRequest
import com.example.appiumint.network.LoginResponse
import com.example.appiumint.network.RetrofitClient
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.appiumint.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class inicio_sesion : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    class InicioSesionActivity : AppCompatActivity() {
        private val sharedViewModel: SharedViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.inicio_sesion)

            val emailInput: EditText = findViewById(R.id.email)
            val passwordInput: EditText = findViewById(R.id.password)
            val signInButton: Button = findViewById(R.id.sign_in_button)

            // Configurar evento de clic para el botón de inicio de sesión
            signInButton.setOnClickListener {
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    login(email, password)
                } else {
                    Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun login(email: String, password: String) {
            val request = LoginRequest(email, password)
            RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null && result.success) {
                            Toast.makeText(this@InicioSesionActivity, "Bienvenido", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@InicioSesionActivity, result?.message ?: "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@InicioSesionActivity, "Error en el servidor", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@InicioSesionActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }
}
