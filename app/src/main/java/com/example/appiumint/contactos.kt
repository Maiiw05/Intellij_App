package com.example.appiumint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appiumint.adapters.ContactAdapter
import com.example.appiumint.databinding.FragmentContactBinding
import com.example.appiumint.network.Contact
import com.example.appiumint.network.ContactsResponse
import com.example.appiumint.network.GetContactsRequest
import com.example.appiumint.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context


class contactos : Fragment() {

    private lateinit var binding: FragmentContactBinding
    private lateinit var adapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)

        binding.contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ContactAdapter(contactList) { selectedContact ->
            Toast.makeText(requireContext(), "Seleccionado: ${selectedContact.nombre}", Toast.LENGTH_SHORT).show()
        }
        binding.contactsRecyclerView.adapter = adapter

        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs",   Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("id_usuario", -1)
        if (idUsuario != -1) {
            fetchContacts(idUsuario)
        } else {
            Toast.makeText(requireContext(), "Sesión no válida, por favor inicia sesión", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun fetchContacts(idUsuario: Int) {
        val request = GetContactsRequest(idUsuario)
        RetrofitClient.instance.getContacts(request).enqueue(object : Callback<ContactsResponse> {
            override fun onResponse(call: Call<ContactsResponse>, response: Response<ContactsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { contactList ->
                        this@contactos.contactList.clear()
                        this@contactos.contactList.addAll(contactList)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al obtener contactos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ContactsResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

