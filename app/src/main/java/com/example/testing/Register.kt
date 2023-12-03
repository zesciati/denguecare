package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.databinding.ActivityRegisterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.signUp.setOnClickListener {
            val signupFullname = binding.NamaLengkap.text.toString()
            val signupEmail = binding.Email.text.toString()
            val signupNoHp = binding.noHp.text.toString()
            val signupUsername = binding.Username.text.toString()
            val signupPassword = binding.Password.text.toString()
//            val signupConfirmpassword = binding.ConfirmPassword.text.toString()
            val dataImage = ""

            if (signupFullname.isNotEmpty() && signupEmail.isNotEmpty() && signupNoHp.isNotEmpty() && signupUsername.isNotEmpty() && signupPassword.isNotEmpty() ){
                signupUser(signupFullname,signupEmail,signupNoHp,signupUsername,signupPassword,dataImage)
            } else{
                Toast.makeText(this@Register,"All fields are mandatory",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun signupUser(fullname: String,username: String,password: String,email: String,noHp: String,dataImage: String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.exists()){
                    val id = databaseReference.push().key
                    val userData = UserData(id,fullname,email,noHp,username,password,dataImage )
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@Register,"Sucessful Register",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Register, Login::class.java))
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    finish()
                }else {
                    Toast.makeText(this@Register,"Users already exists",Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Register,"Database Error: ${databaseError.message}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun fKeLogin(view: View){
        val intentKeLogin = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentKeLogin)
        finish()
    }

    fun fKembali(view: View){
        val intentBalik = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentBalik)
        finish()
    }
}