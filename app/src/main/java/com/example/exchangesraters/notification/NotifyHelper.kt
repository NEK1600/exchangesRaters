package com.example.exchangesraters.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.exchangesraters.MainActivity
import com.example.exchangesraters.R
import com.example.exchangesraters.data.model.RatesModel
import com.example.exchangesraters.data.remote.ApiClient
import com.example.exchangesraters.viewModel.RepositoryRates
import com.example.exchangesraters.viewModel.ViewModelFactory
import com.example.exchangesraters.viewModel.ViewModelRates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class NotifyHelper(context: Context, params: WorkerParameters):
    CoroutineWorker(context, params) {
    companion object {
        //константы для канала уведомлений
        const val CHANNEL_ID = "channel_id"
        val NOTIFICATION_ID = Random.nextInt(1,10)//1
    }
    override suspend fun doWork(): Result= coroutineScope {
        try {
            lateinit var mpref: SharedPreferences
            mpref = applicationContext.getSharedPreferences("TABLE", Context.MODE_PRIVATE)
            Log.d("test2", "do work сработал")
            //число долара на сегодня
            var strOne = ""
            //число долара на сегодня
            var doubleOne = 0.00
            //форматирую время для отправки на сервер
            var time : String= SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
            Log.d("test2", "Число сегодня ${time}")
            val retrofitService = ApiClient.create().getRates(time,time)
            if (retrofitService.isSuccessful) {
                strOne = retrofitService?.body()?.list?.get(0)?.value.toString()
                strOne = strOne.replace(",", ".")
                doubleOne = strOne.toDouble()
                var number = mpref.getString("courseString","0.0")?.toString()
                var numberTwo = number?.toDouble()
                Log.d("test2", "число пользователя $numberTwo число долара " +
                        "$doubleOne дата сегодня $time")
                    /*проверяю больше или меньше курс долара чем заданое число польщователя и
                    вывожу уведомление*/
                    if (numberTwo!!<doubleOne){
                        notifyFun()
                    }else{
                        Log.d("test2", "меньше заданного в приложении")
                    }
            }else{
                Log.d("test2","Провал ${retrofitService.body()}")
            }

        } catch (ex: Exception) {
            Log.d("test", ex.toString())
            Result.failure()
        }
        Result.success()
    }
    //канал уведомлений
    fun notifyFun(){
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0, intent, 0
        )
        val notification = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        ).setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("New Course")
        .setContentText("Subscribe on the channel")
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName = "Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }


        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }

}
