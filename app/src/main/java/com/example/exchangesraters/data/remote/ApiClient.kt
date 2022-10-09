package com.example.exchangesraters.data.remote

import com.example.exchangesraters.data.model.RatesModel
import com.example.exchangesraters.data.model.RecordList
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiClient {
    @GET("XML_dynamic.asp?VAL_NM_RQ=R01235")
    suspend fun getRates(
        @Query("date_req1") timeNow:String,
        @Query("date_req2") timeOld:String
    ) : Response<RatesModel>//Response<RecordList>//
//http://cbr.ru/scripts/XML_dynamic.asp?date_req1=01/09/2022&date_req2=30/10/2022&VAL_NM_RQ=R01235

    companion object {
        var BASE_URL = "https://cbr.ru/scripts/"

        fun create() : ApiClient {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiClient::class.java)

        }
    }

}























