package com.example.testing

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.testing.databinding.ActivityEditprofileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        storageBase = Firebase.storage
        var reference = storageBase.reference
        var imagesRef: StorageReference? = reference.child("images")
        var spaceRef = reference.child("images/space.jpg")

//        imagesRef = spaceRef.parent
//        val rootRef = spaceRef.root
//
//        val earthRef = spaceRef.parent?.child("earth.jpg")
//        val nullRef = spaceRef.root.parent

        spaceRef.path
        spaceRef.name
        spaceRef.bucket

        val mountainsRef = reference.child("mountains.jpg")
        val mountainImagesRef = reference.child("images/mountains.jpg")

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                fileName = getFileNameFromUri(imageUri).orEmpty()
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

        // Convert the image to bytes
        binding.gambarditprofile.isDrawingCacheEnabled = true
        binding.gambarditprofile.buildDrawingCache()
        val dataImage = (binding.gambarditprofile.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        dataImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images") // Change this path as needed
        val imageRef = imagesRef.child("mountains.jpg") // Change the file name as needed

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // Image upload successful, now you can store other user data in the database
            // ...

            var file = Uri.fromFile(File("path/to/images/rivers.jpg"))
            val riversRef = storageRef.child("images/${file.lastPathSegment}")
            uploadTask = riversRef.putFile(file)

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }


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
            } else if (dataImage == null) {
                Toast.makeText(
                    applicationContext,
                    "Gambar profil tidak boleh kosong",
                    Toast.LENGTH_LONG
                ).show()
            }

            val originalFileName = file.lastPathSegment
            val storageRef = Firebase.storage.reference.child("images/$originalFileName")

            uploadTask = storageRef.putFile(file)

            uploadTask.pause()
            uploadTask.resume()
            uploadTask.cancel()



            // masukkin data ke database
            val id = databaseReference.push().key

            val userData = UserData(id, dataNama, dataEmail, dataNoHp, dataUser)
            databaseReference.child(id!!).setValue(userData)
            Toast.makeText(this, "Sucessful Change data", Toast.LENGTH_SHORT).show()

        }
        fun fKeProfile(view: View) {
            val intentKeProfile = Intent(this, Profile::class.java)
            startActivity(intentKeProfile)
        }
    }
}