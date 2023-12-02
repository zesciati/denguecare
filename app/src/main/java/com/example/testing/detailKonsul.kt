package com.example.testing

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class detailKonsul : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_konsul)

        val chatbutton = findViewById<Button>(R.id.chatbutton1)

        chatbutton.setOnClickListener {
            // Start SecondActivity when the button is clicked
            val intent = Intent(this@detailKonsul, ChatRoom::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }


//        chatbutton.setOnClickListener {
//            openWhatsApp()
//        }

    }

//    fun openWhatsApp() {
//        val phoneNumber = "+6282258864305" // Replace with the recipient's phone number
//
//        val uri = Uri.parse("https://wa.me/$phoneNumber")
//
//        val intent = Intent(Intent.ACTION_VIEW, uri)
//        intent.`package` = "com.whatsapp"
//
//        // Check if WhatsApp is installed
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivity(intent)
//        } else {
//            // If WhatsApp is not installed, show a message or do something else
//            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
//        }
//    }
}