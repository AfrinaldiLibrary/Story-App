package com.example.storyapp.data

import android.content.Context
import android.content.SharedPreferences

class PrefManager(var context: Context) {

    private var pref: SharedPreferences? = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor? = pref?.edit()

    fun setLoggin(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun setToken(token: String?) {
        editor?.putString(TOKEN, token)
        editor?.commit()
    }

    fun isLogin(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun getToken(): String? {
        return pref?.getString("token", "")
    }

    fun removeData() {
        editor?.clear()
        editor?.commit()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        const val TOKEN = "token"
        const val IS_LOGIN = "is_login"
    }
}