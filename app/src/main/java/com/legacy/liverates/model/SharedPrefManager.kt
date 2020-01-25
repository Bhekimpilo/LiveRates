package com.legacy.liverates.model

import android.content.Context
import android.content.SharedPreferences

import android.content.Context.MODE_PRIVATE
import android.os.Build
import androidx.annotation.RequiresApi

internal class SharedPrefManager(context: Context) {
    private val sharedPreferences: SharedPreferences?
    private val SHARED_PREFS_NAME = "LOCAL_CACHE"
    private val SAVED_DATA_NAME = "CashedData"

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun save(data: String) {
        val editor = sharedPreferences!!.edit()
        editor.putString(SAVED_DATA_NAME, data)
        editor.apply()
    }


    fun load(): String {
        val DEFAULT_VALUE = "{}"
        return if (sharedPreferences != null)
            sharedPreferences.getString(SAVED_DATA_NAME, "{}")!!
        else
            DEFAULT_VALUE
    }
}
