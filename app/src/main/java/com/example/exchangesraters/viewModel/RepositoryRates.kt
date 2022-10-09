package com.example.exchangesraters.viewModel

import com.example.exchangesraters.constants.MyVariables
import com.example.exchangesraters.data.remote.ApiClient

class RepositoryRates (private val retrofitService: ApiClient)
{

    suspend fun getAllTermin() =
        retrofitService.getRates(MyVariables.dateFormatedTwo,MyVariables.dateFormatedOne)
}