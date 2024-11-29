package com.example.appiumint.viewmodel

import com.example.appiumint.viewmodel.SharedViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    // LiveData para compartir el número de cuenta seleccionado
    val selectedAccount = MutableLiveData<String>()
}
