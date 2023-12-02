package com.example.testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import com.example.testing.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {

    private lateinit var binding:ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

//        ngambil data dari firebase
//        set data yang diperoleh ke komponen XML memakai binding
//        fKeEditProfile() -> dipanggil
        binding.bKembali1.setOnClickListener{
            val intentKeEditProfile = Intent(this, EditProfile::class.java)
            startActivity(intentKeEditProfile)
        }
    }
    fun kembali(view: View) {
        val intent = Intent(this, BerandaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    fun fKeEditProfile(view: View) {
        val intentKeEditProfile = Intent(this, EditProfile::class.java)
        startActivity(intentKeEditProfile)
    }

}