package com.example.appiumint.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login.php")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("get_user_data.php")
    fun getUserData(@Body request: Map<String, Int>): Call<UserDataResponse>

    @POST("get_movements.php")
    fun getMovements(@Body request: Map<String, Int>): Call<MovementsResponse>

    @POST("get_contacts.php")
    fun getContacts(): Call<List<Contact>>

}

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val id_usuario: Int? = null,
    val id_rol: Int? = null,
    val username: String? = null
)

data class UserDataResponse(
    val success: Boolean,
    val data: UserData?
)

data class UserData(
    val nombre: String,
    val numero_cuenta: String,
    val saldo: Double
)

data class ContactsResponse(
    val success: Boolean,
    val data: List<Contact>?
)

data class Contact(
    val nombre: String,
    val numero_cuenta: String
)



