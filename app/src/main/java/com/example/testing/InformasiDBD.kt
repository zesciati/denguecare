package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class InformasiDBD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informasi)
    }

    fun penjelasan_DBD(view: View) {
        val intent = Intent(this, Penjelasan_DBD::class.java)
        startActivity(intent)
    }

    fun kembaliKeHome(view: View) {
        val intent = Intent(this, BerandaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    fun gejala_DBD(view: View) {
        val intent = Intent(this, Gejala_DBD::class.java)
        startActivity(intent)
    }

    fun Pengobatan_DBD(view: View) {
        val intent = Intent(this, Pengobatan_DBD::class.java)
        startActivity(intent)
    }

    fun Pencegahan_DBD(view: View) {
        val intent = Intent(this, Pencegahan_DBD::class.java)
        startActivity(intent)
    }

    fun Mendeteksi_DBD(view: View) {
        val intent = Intent(this, Mendeteksi_DBD::class.java)
        startActivity(intent)
    }
}