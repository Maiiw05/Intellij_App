package com.example.appiumint.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login.php")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("get_user_data.php")
    fun getUserData(@Body request: GetUserDataRequest): Call<UserDataResponse>

    @POST("get_movements.php")
    fun getMovements(@Body request: Map<String, Int>): Call<MovementsResponse>

    @POST("get_contacts.php")
    fun getContacts(@Body request: GetContactsRequest): Call<ContactsResponse>

        @POST("register_transaction.php")
        fun registerTransaction(@Body request: RegisterTransactionRequest): Call<RegisterTransactionResponse>
    }




data class LoginRequest(val username: String, val password: String)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val id_usuario: Int?,
    val id_rol: Int?,
    val rol: String?,
    val username: String?
)

data class GetUserDataRequest(val id_usuario: Int)

data class UserDataResponse(
    val success: Boolean,
    val message: String,
    val data: UserData?
)

data class UserData(
    val id_usuario: Int?,
    val username: String?,
    val nombre: String?,
    val apellido: String?,
    val email: String?,
    val telefono: String?,
    val rol: String?
)

data class GetContactsRequest(val id_usuario: Int)
data class ContactsResponse(
    val success: Boolean,
    val data: List<Contact>?
)

data class Contact(
    val nombre: String,
    val numero_cuenta: String
)

data class RegisterTransactionRequest(
    val idUsuario: Int,
    val numeroCuentaDestino: Int,
    val monto: Double
)

data class RegisterTransactionResponse(
    val success: Boolean,
    val message: String
)