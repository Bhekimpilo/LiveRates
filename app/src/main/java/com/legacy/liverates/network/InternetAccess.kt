package com.legacy.liverates.network

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity


class InternetAccess {

    fun isAvailable(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }


}