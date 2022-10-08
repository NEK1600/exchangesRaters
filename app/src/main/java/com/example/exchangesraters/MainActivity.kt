package com.example.exchangesraters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.exchangesraters.adapters.AdapterRates
import com.example.exchangesraters.constant.MyVariables
import com.example.exchangesraters.data.model.RatesModel
import com.example.exchangesraters.data.model.RecordList
import com.example.exchangesraters.data.remote.ApiClient
import com.example.exchangesraters.databinding.ActivityMainBinding
import com.example.exchangesraters.notification.Alarm
import com.example.exchangesraters.notification.MyService
import com.example.exchangesraters.notification.NotifyHelper
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
    var listRates = mutableListOf<RecordList>()
    var curs:String=""
    var cursTwo:String=""
    //здесь хранится заданое пользователем значение
    //var cursThree:Double=0.00
    private lateinit var pref: SharedPreferences

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        initConnect()
        initPref()
        initNotify()

    }

    fun initRecycler(){
        rcViewRates = binding.rvRates
        adapter = AdapterRates()
        rcViewRates.adapter = adapter
        var linearLayoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        linearLayoutManager.setReverseLayout(true)
        linearLayoutManager.setStackFromEnd(true)
        rcViewRates.layoutManager = linearLayoutManager
    }

    fun initConnect(){
        MyVariables.dateFormatedOne = SimpleDateFormat("dd/MM/yyyy").format(getDateTimeNow())
        MyVariables.dateFormatedTwo = SimpleDateFormat("dd/MM/yyyy").format(getDateTimeOld())

            val apiInterface = ApiClient.create().getRates(MyVariables.dateFormatedTwo,MyVariables.dateFormatedOne)
            apiInterface.enqueue(object : Callback<RatesModel> {
            override fun onResponse(call: Call<RatesModel>, response: Response<RatesModel>) {
                listRates= response.body()?.list!!.toMutableList()
                adapter.update(listRates)

            }
            override fun onFailure(call: Call<RatesModel>, t: Throwable) {
                //Log.d("test", "OnFailure ${t.toString()}")
            }

        })
    }

    fun initPref(){
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)

    }

    fun initNotify(){
        /*val uploadWorkRequest: WorkRequest =
           OneTimeWorkRequestBuilder<NotifyHelper>()
               .setInitialDelay(15, TimeUnit.MINUTES)
               .build()
        WorkManager
            .getInstance(this)
            .enqueue(uploadWorkRequest)
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .build()*/

        /*val uploadWorkRequestTwo :PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            NotifyHelper::class.java,
            15,
            TimeUnit.MINUTES,
        ).addTag("workTag").build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "MyUniqueWorkName",
            ExistingPeriodicWorkPolicy.KEEP,
            uploadWorkRequestTwo)*/
        val myIntent = Intent(this@MainActivity, MyService::class.java)
        var pendingIntent = PendingIntent.getService(this@MainActivity, 0, myIntent, 0)
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.SECOND, 3)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

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

    fun onClickSave(view: View) {
        curs = binding.edText.text.toString()
        pref.edit().putString("courseString", curs).apply()
        cursTwo = pref.getString("courseString","0").toString()
        MyVariables.cursThree=cursTwo.toDouble()
    }



}
















































