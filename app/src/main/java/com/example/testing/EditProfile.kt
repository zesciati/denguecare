package com.example.testing

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.testing.databinding.ActivityEditprofileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditprofileBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageBase: FirebaseStorage
    private  var userdata: UserData? = null
    private lateinit var storageReference: StorageReference
    private  var imageUri: Uri? = null
    private lateinit var sessionManager: SessionManager
    private var isLoginBySSO = false
    private var dataSessionUser = ""
    private var fileName = ""

//    private lateinit var id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

//        id = intent.getStringExtra("Id")!!
//        Log.d("EditProfile", "onCreate: $id")

//        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.BLUE))
        val receivedIntent = intent
        val receivedUser = receivedIntent.getParcelableExtra<UserData>(Profile.DATAUSER_KEY)

        sessionManager = SessionManager(this)
        // Mendapatkan nilai sesi
        isLoginBySSO = sessionManager.getIsLoginBySSO()
        dataSessionUser = sessionManager.getDataSessionUser().orEmpty()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        storageBase = FirebaseStorage.getInstance()
        storageReference = storageBase.reference.child("images")

//        ngambil data dari firebase




        if (receivedUser != null) {
            // Lakukan sesuatu dengan data yang diterima
            userdata = receivedUser
            updateUIWithData(userdata?.fullname,userdata?.email, userdata?.noHp, userdata?.username, userdata?.image)
        }

//        var imagesRef: StorageReference? = storageBase.reference.child("images")
//        var spaceRef = reference.child("images/space.jpg")

//        imagesRef = spaceRef.parent
//        val rootRef = spaceRef.root
//
//        val earthRef = spaceRef.parent?.child("earth.jpg")
//        val nullRef = spaceRef.root.parent

//        spaceRef.path
//        spaceRef.name
//        spaceRef.bucket

//        val mountainsRef = reference.child("mountains.jpg")
//        val mountainImagesRef = reference.child("images/mountains.jpg")

//        mountainsRef.name == mountainImagesRef.name
//        mountainsRef.path == mountainImagesRef.path


//      Mencari dan menemukan Id
//        ngambil data dari firebase
//        set data yang diperoleh ke komponen XML memakai binding

        binding.camera.setOnClickListener {

            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        // Button
        binding.updateprofile.setOnClickListener {
            editProfile()
        }
    }
    private fun updateUIWithData(dataNama: String?,  dataEmail: String?, dataNoHp: String?, dataUser: String?, dataImage: String? ) {
        // Update your XML components using data binding
        // For example, if you have binding variables in your XML layout, update them like this:

        if(dataUser != null && dataNama != null && dataEmail != null && dataNoHp != null && dataImage != null ){
            binding.NamaLengkap.setText(dataNama)
            binding.Email.setText(dataEmail)
            binding.noHp.setText(dataNoHp)
            binding.Username.setText(dataUser)
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

        if (userdata?.image?.isNotEmpty() == true){

            val imageRef = userdata?.image?.let { storageReference.child(it) }

            // Download URL gambar dari Firebase Storage
            imageRef?.downloadUrl?.addOnSuccessListener { uri ->
                // Menggunakan Glide untuk memuat gambar ke dalam ImageView (Glide library)
                Glide.with(this@EditProfile)
                    .load(uri)
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_person_24)
                    .into(binding.gambarditprofile)
            }?.addOnFailureListener {
                // Handle kegagalan saat mengambil URL gambar
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                imageUri = uri
                fileName = imageUri?.lastPathSegment.orEmpty()
                Log.d("Zaky", fileName)
                binding.gambarditprofile.setImageURI(imageUri)

                // Sekarang, Anda memiliki nama file (fileName) yang dapat digunakan sesuai kebutuhan.
            }
        }
    }
    private fun getFileNameFromUri(uri: Uri): String? {
        val contentResolver = contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)

        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }
    }

    // Mendapatkan DATA
    fun editProfile() { // rawan error di line 53
//        var dataImage = binding.gambarditprofile.drawable; // mencari ambil gambar dan validasi gambar
        var dataNama = binding.NamaLengkap.text.trim().toString();
        var dataEmail = binding.Email.text.trim().toString();
        var dataNoHp = binding.noHp.text.trim().toString();
        var dataUser = binding.Username.text.trim().toString();




        // MEMVALIDASI DATA
        // buat validasi data agar tidak null / kosong jika gitu akan keluar toast
        if (dataNama.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "Nama Lengkap anda tidak boleh kosong",
                Toast.LENGTH_LONG
            ).show()
        } else if (dataEmail.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "Email anda tidak boleh kosong",
                Toast.LENGTH_LONG
            ).show()

        } else if (dataNoHp.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "Nomor Handphone anda tidak boleh kosong",
                Toast.LENGTH_LONG
            ).show()
        } else if (dataUser.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "User anda tidak boleh kosong",
                Toast.LENGTH_LONG
            ).show()
        } else {
            if(!dataSessionUser.isNullOrEmpty()){
                userdata?.fullname = dataNama
                userdata?.email = dataEmail
                userdata?.noHp = dataNoHp
                userdata?.username = dataUser
                userdata?.image = fileName
                val newData = mapOf(
                    "fullname" to userdata?.fullname,
                    "email" to userdata?.email,
                    "noHp" to userdata?.noHp,
                    "username" to userdata?.username,
                    "image" to fileName,
                    "id" to userdata?.id,
                    "password" to userdata?.password
                )
                imageUri?.let { uploadImage(it) }
                userdata?.id?.let { updateData(it, newData) }

//                if (isLoginBySSO === true) getUserIdByEmail(dataSessionUser, newData )
//                else getUserIdByUserName(dataSessionUser, newData )

            }
        }

//            val originalFileName = file.lastPathSegment
//            val storageRef = Firebase.storage.reference.child("images/$originalFileName")
//
//            uploadTask = storageRef.putFile(file)
//
//            uploadTask.pause()
//            uploadTask.resume()
//            uploadTask.cancel()



            // masukkin data ke database
//            val id = databaseReference.push().key
//
//            val userData = UserData(id, dataNama, dataEmail, dataNoHp, dataUser)
//            databaseReference.child(id!!).setValue(userData)
//            Toast.makeText(this, "Sucessful Change data", Toast.LENGTH_SHORT).show()



    }

    fun getUserIdByEmail(query: String, newData:  Map<String, Any>){

        databaseReference.orderByChild("email").equalTo(query).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val userId = userSnapshot.key
                        // Lakukan pembaruan data menggunakan userId yang didapatkan
                        if (userId != null) updateData(userId, newData)

                        return
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle kesalahan query
            }
        })
    }

    fun getUserIdByUserName(query: String, newData:  Map<String, Any>){
        databaseReference.orderByChild("username").equalTo(query).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val userId = userSnapshot.key
                        // Lakukan pembaruan data menggunakan userId yang didapatkan
                        if (userId != null) updateData(userId, newData)

                        return
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle kesalahan query
            }
        })
    }
    fun updateData(userId: String, newData: Map<String, *>) {
        val usersReference = FirebaseDatabase.getInstance().getReference("users")
        usersReference.child(userId).updateChildren(newData)
            .addOnSuccessListener {
                // Data berhasil diperbarui
                // Di EditProfile.kt
                val intent = Intent()
                intent.putExtra(Profile.DATAUSER_KEY, userdata) // imagePath adalah path gambar yang telah diperbarui
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            .addOnFailureListener { e ->
                // Handle kegagalan pembaruan
                Toast.makeText(this, "Failed to Upload Data", Toast.LENGTH_SHORT).show()
            }
    }



    private fun uploadImage(imageUri: Uri) {
        val imageName = fileName // Ganti dengan nama file yang Anda inginkan
        val imageRef = storageReference.child(imageName)

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Gambar berhasil diunggah
                // Dapatkan URL gambar yang diunggah
                imageRef.downloadUrl.addOnSuccessListener { uri ->
//                    val imageUrl = uri.toString()
                    Toast.makeText(this, "Sucessful Upload Image", Toast.LENGTH_SHORT).show()
                    // Gunakan URL gambar sesuai kebutuhan Anda
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_SHORT).show()
                // Handle kegagalan unggah
            }
    }


    fun fKeProfile(view: View) {
//        val intentKeProfile = Intent(this, Profile::class.java)
//        startActivity(intentKeProfile)
        onBackPressed()
    }
}