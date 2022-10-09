package com.example.exchangesraters.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangesraters.data.model.RatesModel
import com.example.exchangesraters.data.model.RecordList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ViewModelRates (private val repositoryRates: RepositoryRates) : ViewModel() {
    val mutableLiveData = MutableLiveData<RatesModel>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    //через репозиторий помещаю запрос в MutableLiveData
    fun getAllRates()= viewModelScope.launch(Dispatchers.IO) {
        val responce = repositoryRates.getAllTermin()
        mutableLiveData.postValue(responce.body())
    }
    //Использую flow для галочки
    fun getAllRatesTwo()= flow {
        emit(repositoryRates.getAllTermin())
    }
    //обрабатываю ошибку
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }



}