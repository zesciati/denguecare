package com.example.testing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class HasilDiagnosis : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_diagnosis)

        val KUNCI = "diagnosis"
        val pesan = intent.getStringExtra(KUNCI)
        val diagnosis = findViewById<TextView>(R.id.diagnosis2)
        diagnosis.text = pesan
    }

    fun kembaliHD(view: View){
        val intent = Intent(this@HasilDiagnosis, Diagnosis::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    fun fKeMain(view: View){
        val intentKeMain = Intent(this, BerandaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentKeMain)
        finish()
    }
}