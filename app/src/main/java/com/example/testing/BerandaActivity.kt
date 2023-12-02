package com.example.testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import java.util.logging.Logger.global


class BerandaActivity : AppCompatActivity() {

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)


         id = intent.getStringExtra("Id")!!


        Log.d("BerandaActivity", "onCreate: $id")

    }

        fun fKonsul(view: View) {
            // Start SecondActivity when the button is clicked
            val intent = Intent(this@BerandaActivity, Pilihkonsul::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        fun fDiagnosis(view: View) {
            // Start SecondActivity when the button is clicked
            val intent = Intent(this@BerandaActivity, Diagnosis::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        fun fInfo(view: View) {
            val intent = Intent(this@BerandaActivity, InformasiDBD::class.java)
            startActivity(intent)
        }

        fun fprofile(view: View) {
            val intent = Intent(this@BerandaActivity, Profile::class.java)
            startActivity(intent)
        }


    }

