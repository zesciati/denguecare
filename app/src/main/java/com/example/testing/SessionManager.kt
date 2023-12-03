package com.example.testing

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MySession", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Nama-nama kunci untuk data sesi
    private val KEY_IS_LOGIN_BY_SSO = "isLoginBySSO"
    private val KEY_DATA_SESSION_USER = "dataSessionUser"

    // Fungsi untuk menyimpan sesi login
    fun saveLoginSession(isLoginBySSO: Boolean, dataSessionUser: String) {
        editor.putBoolean(KEY_IS_LOGIN_BY_SSO, isLoginBySSO)
        editor.putString(KEY_DATA_SESSION_USER, dataSessionUser)
        editor.apply()
    }

    // Fungsi untuk mendapatkan nilai isLoginBySSO dari sesi
    fun getIsLoginBySSO(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGIN_BY_SSO, false)
    }

    // Fungsi untuk mendapatkan nilai dataSessionUser dari sesi
    fun getDataSessionUser(): String? {
        return sharedPreferences.getString(KEY_DATA_SESSION_USER, null)
    }

    // Fungsi untuk logout dan menghapus data sesi
    fun logout() {
        editor.clear()
        editor.apply()
    }
}
