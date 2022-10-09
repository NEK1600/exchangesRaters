package com.example.exchangesraters

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.exchangesraters.adapters.AdapterRates
import com.example.exchangesraters.constants.MyVariables
import com.example.exchangesraters.data.model.RatesModel
import com.example.exchangesraters.data.remote.ApiClient
import com.example.exchangesraters.databinding.ActivityMainBinding
import com.example.exchangesraters.notification.NotifyHelper
import com.example.exchangesraters.viewModel.RepositoryRates
import com.example.exchangesraters.viewModel.ViewModelFactory
import com.example.exchangesraters.viewModel.ViewModelRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var rcViewRates: RecyclerView
    lateinit var adapter: AdapterRates
    //через нее добавляю число пользователя в таблицу
    var curs:String=""
    //через нее получаю число пользователя
    var cursTwo:String=""
    private lateinit var pref: SharedPreferences
    private val retrofitService = ApiClient.create()
    lateinit var viewModel: ViewModelRates
    //Нужны для начальной точки календаря
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelFactory(RepositoryRates(retrofitService)))
            .get(ViewModelRates::class.java)
        initRecycler()
        initConnect()
        initPref()
        initNotify()
    }

    //настраиваю recycler
    fun initRecycler(){
        rcViewRates = binding.rvRates
        adapter = AdapterRates()
        rcViewRates.adapter = adapter
        var linearLayoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        linearLayoutManager.setReverseLayout(true)
        linearLayoutManager.setStackFromEnd(true)
        rcViewRates.layoutManager = linearLayoutManager
    }

    //используя mvvm и coroutines делаю запрос на сервер
    fun initConnect(){
        MyVariables.dateFormatedOne = SimpleDateFormat("dd/MM/yyyy").format(getDateTimeNow())
        MyVariables.dateFormatedTwo = SimpleDateFormat("dd/MM/yyyy").format(getDateTimeOld())
        viewModel.mutableLiveData.observe(this, {it ->
            adapter.update(it.list)
        })
        viewModel.errorMessage.observe(this, {
            Log.d("test2",it)
        })
        viewModel.getAllRates()
    }

    //подключаю sharedPreferences
    fun initPref(){
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
    }

    //Подключаю workManager
    fun initNotify(){
        //создаю ограничения без которых он не работает
        val constraints = Constraints.Builder()
            //.setRequiresDeviceIdle(true )
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        //Каждый день запрашиваю доступ на сервер и вывожу уведомление по необходимости
        val uploadWorkRequestTwo :PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            NotifyHelper::class.java,
            24,
            TimeUnit.HOURS,
        ).setConstraints(constraints).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "MyUniqueWorkName",
            ExistingPeriodicWorkPolicy.KEEP,
            uploadWorkRequestTwo)

    }


    //возращает время на данный моменрт
    fun getDateTimeNow(): Date {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        cal.set(year, month, day, hour, minute)
        return cal.time
    }
    //возращает время на 30 дней назад
    private fun getDateTimeOld(): Date {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        var dayT = day-30
        cal.set(year, month, dayT, hour, minute)
        return cal.time
    }
    //сохраняю число поьзователя в sharedPreferences
    fun onClickSave(view: View) {
        curs = binding.edText.text.toString()
        pref.edit().putString("courseString", curs).apply()
        cursTwo = pref.getString("courseString","0").toString()
    }



}
















































