package com.example.testing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView

class Pilihkonsul : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilihkonsul)

        val cardKonsul0 = findViewById<CardView>(R.id.konsultan0)
        val cardKonsul1 = findViewById<CardView>(R.id.konsultan1)
        val cardKonsul2 = findViewById<CardView>(R.id.konsultan2)
        val cardKonsul3 = findViewById<CardView>(R.id.konsultan3)

        val backKonsul = findViewById<View>(R.id.backKonsul)

        backKonsul.setOnClickListener {
            // Start SecondActivity when the button is clicked
            val intent = Intent(this@Pilihkonsul, BerandaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        cardKonsul0.setOnClickListener{
            showPopUpDialog(this, 0)

        }
        cardKonsul1.setOnClickListener{
            showPopUpDialog(this, 1)

        }
        cardKonsul2.setOnClickListener{
            showPopUpDialog(this, 2)

        }
        cardKonsul3.setOnClickListener{
            showPopUpDialog(this, 3)

        }

    }

}