package com.example.testing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        auth = FirebaseAuth.getInstance();
         sessionManager = SessionManager(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("960560562916-fjn561dd4v7o8ltifkdleefjejkbrik7.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        findViewById<SignInButton>(R.id.googlesignInBtn).setOnClickListener {
            googleSignIn();
        }

        binding.bLogin.setOnClickListener {
            val loginUsername = binding.loginUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            if (loginUsername.isNotEmpty() && loginPassword.isNotEmpty()){
                loginUser(loginUsername,loginPassword)
            } else{
                Toast.makeText(this@Login,"All fields are mandatory",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun googleSignIn() {
        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        result ->

        if(result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            manageResults(task);
        }

    }

    private fun manageResults(task: Task<GoogleSignInAccount>) {
        val account : GoogleSignInAccount? = task.result

        if (account != null){

            val email = account.email
            loginBySSO("","","",email.toString(),"","")

            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            auth.signInWithCredential(credential).addOnCompleteListener {

                if(task.isSuccessful){

                    val intent = Intent(this, BerandaActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(this,"Account signed",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    fun fKeRegister(view: View) {
        val intentKeRegister = Intent(this, Register::class.java)
        startActivity(intentKeRegister)
    }

    private fun loginBySSO(fullname: String,username: String,password: String,email: String,noHp: String,confirmpassword: String){
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    val id = databaseReference.push().key
                    val userData =
                        UserData(id, fullname, email, noHp, username, password, confirmpassword)
                    databaseReference.child(id!!).setValue(userData)
                    sessionManager.saveLoginSession(true, email)

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Login,"Database Error: ${databaseError.message}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loginUser(username:String,password:String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData != null && userData.password == password) {
                            Toast.makeText(this@Login, "Login successful", Toast.LENGTH_SHORT).show()
                            sessionManager.saveLoginSession(false, username)

                            // Pastikan bahwa intent dan start activity dijalankan di thread UI
                            runOnUiThread {
                                Log.d("LoginActivity", "Running on UI thread")
                                val keBeranda = Intent(this@Login, BerandaActivity::class.java)
                                keBeranda.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                keBeranda.putExtra("Id", userData.id)
                                Log.d("Login", "onDataChange: ${userData.id}" )
                                startActivity(keBeranda)
                                finish()
                            }

                            return
                        }
                    }
                }
                Toast.makeText(this@Login, "Login failed", Toast.LENGTH_SHORT).show()
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Login,"Database Error: ${databaseError.message}",Toast.LENGTH_SHORT).show()
            }
        })
    }
}