package com.example.exchangesraters.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//вспомагательный клас для создания view model
class ViewModelFactory constructor(private val repository: RepositoryRates): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ViewModelRates::class.java)) {
            ViewModelRates(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}