package com.example.appiumint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appiumint.adapters.ContactAdapter
import com.example.appiumint.databinding.FragmentContactBinding
import com.example.appiumint.network.Contact
import com.example.appiumint.network.RetrofitClient
import com.example.appiumint.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class contactos : Fragment() {

    private lateinit var binding: FragmentContactBinding
    private lateinit var adapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()
    private val sharedViewModel: SharedViewModel by activityViewModels() // ViewModel compartido

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)

        // Configurar RecyclerView
        binding.contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ContactAdapter(contactList) { selectedContact ->
            // Callback para manejar clics en contactos
            sharedViewModel.selectedAccount.value = selectedContact.numero_cuenta // Guardar en ViewModel
        }
        binding.contactsRecyclerView.adapter = adapter

        // Obtener datos de contactos
        fetchContacts()

        // Configuración del botón continuar
        binding.continueButton.setOnClickListener {
            val selectedContact = adapter.getSelectedContact()
            if (selectedContact != null) {
                Toast.makeText(
                    requireContext(),
                    "Seleccionado: ${selectedContact.nombre}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(), "Selecciona un contacto primero", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun fetchContacts() {
        RetrofitClient.instance.getContacts().enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                if (response.isSuccessful) {
                    contactList.clear()
                    response.body()?.let { contactList.addAll(it) }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Error al cargar contactos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

