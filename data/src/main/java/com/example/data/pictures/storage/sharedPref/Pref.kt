package com.example.data.pictures.storage.sharedPref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson


class Pref private constructor() {

    init {
        //Prevent form the reflection api.
        if (instance != null) {
            throw AssertionError("Use getInstance() method to get the single instance of this class.")
        }
    }

    //Make singleton from serialize and deserialize operation.
    protected fun readResolve(): Pref? {
        return getInstance()
    }


    //====================================================================================

    private fun getPrefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }


    fun saveAccountInfo(context: Context, loginResponce: String?) {
        val prefsEditor = getPrefs(context).edit()
        val gson = Gson()
        val json = gson.toJson(loginResponce)
        prefsEditor.putString(PREF_ACCOUNT, json)
        prefsEditor.apply()
    }

    fun getAccountUser(context: Context): String? {
        val gson = Gson()
        val json = getPrefs(context).getString(PREF_ACCOUNT, "")
        if (json!!.isEmpty()) {
            return null
        }
        return json

    }


    companion object {

        private val PREF_ACCOUNT = "UserAccount"

        //================================== SINGLETON ==========================================

        @Volatile
        private var instance: Pref? = null

        fun getInstance(): Pref? {
            if (instance == null) {
                synchronized(Pref::class.java) {
                    if (instance == null) {
                        instance = Pref()
                    }
                }
            }
            return instance
        }
    }

}
