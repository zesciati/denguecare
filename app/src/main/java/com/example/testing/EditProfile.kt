package com.example.testing

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.testing.databinding.ActivityEditprofileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditprofileBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var id: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        id = intent.getStringExtra("Id")!!


        Log.d("EditProfile", "onCreate: $id")

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.BLUE))

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

//      Mencari dan menemukan Id

//        ngambil data dari firebase
//        set data yang diperoleh ke komponen XML memakai binding

       binding.camera.setOnClickListener {

            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        // Button
        binding.updateprofile.setOnClickListener{
            editProfile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.gambarditprofile.setImageURI(data?.data)
    }
    // Mendapatkan DATA
    fun editProfile(){ // rawan error di line 53
        var dataImage = binding.gambarditprofile.drawable; // mencari ambil gambar dan validasi gambar
        var dataNama = binding.NamaLengkap.text.trim().toString();
        var dataEmail = binding.Email.text.trim().toString();
        var dataNoHp = binding.noHp.text.trim().toString();
        var dataUser = binding.Username.text.trim().toString();

        // MEMVALIDASI DATA
        // buat validasi data agar tidak null / kosong jika gitu akan keluar toast
        if(dataNama.isNullOrEmpty() ){
            Toast.makeText(applicationContext, "Nama Lengkap anda tidak boleh kosong", Toast.LENGTH_LONG).show()
        }else if(dataEmail.isNullOrEmpty()){Toast.makeText(applicationContext, "Email anda tidak boleh kosong", Toast.LENGTH_LONG).show()

        }else if (dataNoHp.isNullOrEmpty()){
            Toast.makeText(applicationContext, "Nomor Handphone anda tidak boleh kosong", Toast.LENGTH_LONG).show()
        }else if (dataUser.isNullOrEmpty()){
            Toast.makeText(applicationContext, "User anda tidak boleh kosong", Toast.LENGTH_LONG).show()
        }else if (dataImage == null){
            Toast.makeText(applicationContext, "Gambar profil tidak boleh kosong", Toast.LENGTH_LONG).show()
        }


        // masukkin data ke database
        val id = databaseReference.push().key

        val userData = UserData(id,dataNama,dataEmail,dataNoHp,dataUser)
        databaseReference.child(id!!).setValue(userData)
        Toast.makeText(this,"Sucessful Change data",Toast.LENGTH_SHORT).show()
    }
    fun fKeProfile(view: View){
        val intentKeProfile = Intent(this, Profile::class.java)
        startActivity(intentKeProfile)
    }
}