package com.example.testing

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.testing.databinding.ActivityProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class Profile : AppCompatActivity() {

    private lateinit var binding:ActivityProfileBinding
    private lateinit var sessionManager : SessionManager
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var storageBase: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private  var userData: UserData? = null

    private var isLoginBySSO = false
    private var dataSessionUser = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        sessionManager = SessionManager(this)
        // Mendapatkan nilai sesi
         isLoginBySSO = sessionManager.getIsLoginBySSO()
         dataSessionUser = sessionManager.getDataSessionUser().orEmpty()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        storageBase = FirebaseStorage.getInstance()
        storageReference = storageBase.reference.child("images")

//        ngambil data dari firebase
        if(!dataSessionUser.isNullOrEmpty()){
            if (isLoginBySSO === true) getUserProfilebyEmail(dataSessionUser)
            else getUserProfilebyUsername(dataSessionUser)

        }

//        fKeEditProfile() -> dipanggil



    }


    fun getUserProfilebyEmail(query: String){
        databaseReference.orderByChild("email").equalTo(query).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    //        set data yang diperoleh ke komponen XML memakai binding
                    for (userSnapshot in dataSnapshot.children) {
                        // Get the value of UserData class from the snapshot
                        val data = userSnapshot.getValue(UserData::class.java)
                        if (data != null ){
                            userData = data
                            // Now, you can use userData to access specific fields
                            val dataNama = userData?.fullname.orEmpty()
                            val dataEmail = userData?.email.orEmpty()
                            val dataNoHp = userData?.noHp.orEmpty()
                            val dataUser = userData?.username.orEmpty()
                            val dataImage = userData?.image.orEmpty()
                            // ... access other fields as needed

                            // Update your XML components using data binding
                            updateUIWithData(dataNama, dataEmail, dataNoHp, dataUser, dataImage)
                        }

                    }
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Profile,"Database Error: ${databaseError.message}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getUserProfilebyUsername(query: String){

        databaseReference.orderByChild("username").equalTo(query).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    //        set data yang diperoleh ke komponen XML memakai binding
                    for (userSnapshot in dataSnapshot.children) {
                        // Get the value of UserData class from the snapshot
                        val data = userSnapshot.getValue(UserData::class.java)
                        if (data != null ){
                            userData = data
                            // Now, you can use userData to access specific fields
                            val dataNama = userData?.fullname.orEmpty()
                            val dataEmail = userData?.email.orEmpty()
                            val dataNoHp = userData?.noHp.orEmpty()
                            val dataUser = userData?.username.orEmpty()
                            val dataImage = userData?.image.orEmpty()
                            // ... access other fields as needed

                            // Update your XML components using data binding
                            updateUIWithData(dataNama, dataEmail, dataNoHp, dataUser, dataImage)
                        }
                    }
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Profile,"Database Error: ${databaseError.message}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUIWithData(dataNama: String?,  dataEmail: String?, dataNoHp: String?, dataUser: String?, dataImage: String? ) {
        // Update your XML components using data binding
        // For example, if you have binding variables in your XML layout, update them like this:

        if(dataUser != null && dataNama != null && dataEmail != null && dataNoHp != null && dataImage != null ){
            binding.namaLengkap.text = dataNama
            binding.email.text = dataEmail
            binding.noPonsel.text = dataNoHp
            binding.Username.text = dataUser
            dataImage?.let {
//                val imagesRef: StorageReference = storageReference.child("images").child(it)
//                Glide.with(this@Profile)
//                    .load(imagesRef)
//                    .into(binding.gambarProfile) // Replace with the ID of your ImageView
                    getImageFromStorage()
            }

        }

    }

    private fun getImageFromStorage() {
        // Mendapatkan referensi dari Firebase Storage

        if (userData?.image?.isNotEmpty() == true){

            val imageRef = userData?.image?.let { storageReference.child(it) }

            // Download URL gambar dari Firebase Storage
            imageRef?.downloadUrl?.addOnSuccessListener { uri ->
                // membersihkan cache untuk URL tertentu
                // TODO memunculkan gambar


                // Menggunakan Glide untuk memuat gambar ke dalam ImageView (Glide library)
                Glide.with(this@Profile)
                    .load(uri)

                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_person_24)
                    .into(binding.gambarProfile)
            }?.addOnFailureListener {
                // Handle kegagalan saat mengambil URL gambar
            }
        }

    }

    fun kembali(view: View) {
//        val intent = Intent(this, BerandaActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
//        finish()
        onBackPressed()
    }

    fun fKeEditProfile(view: View) {
        val intentKeEditProfile = Intent(this, EditProfile::class.java)
        intentKeEditProfile.putExtra(DATAUSER_KEY,userData)
        startActivityForResult(intentKeEditProfile, EDIT_PROFILE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Data yang diperbarui dari EditProfile.kt
            val receivedUser = data?.getParcelableExtra<UserData>(DATAUSER_KEY)
//            val updatedImagePath = data?.getStringExtra("updatedImage")
//
//            // Perbarui gambar di sini
            if (receivedUser != null) {
                userData = receivedUser
                userData?.image?.let { Log.d("Zaky", it+"Profile") }
                updateUIWithData(receivedUser.fullname, receivedUser.email, receivedUser.noHp, receivedUser.username, receivedUser.image)
            }
        }
    }
    companion object{
        const val DATAUSER_KEY = "datauser_key"
        const val EDIT_PROFILE_REQUEST = 1 // Angka bebas, tetapi unik

    }

}