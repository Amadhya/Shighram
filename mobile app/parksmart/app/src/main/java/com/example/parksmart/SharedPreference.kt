package com.example.parksmart

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("parkSmartData", Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(KEY_NAME, value)

        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {

        return sharedPref.getString(KEY_NAME, null)
    }

    fun clearSharedPreference() {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.clear()
        editor.apply()
    }

    fun checkKey(KEY_NAME: String): Boolean{
        return sharedPref.contains(KEY_NAME)
    }

}