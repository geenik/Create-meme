package com.example.create_meme
import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.example.create_meme.models.image
import com.example.create_meme.models.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.util.*

class repository(val application: Application)  {
    private val storage = Firebase.storage
    private val firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore=FirebaseFirestore.getInstance()
    val uid=firebaseAuth.currentUser?.uid
    val userref= uid?.let {
        firestore.collection("users").document(it)
    }
    suspend fun uploadImage(bitmap: Bitmap, name:String) {
         //save file in local
        val uri=saveImageToGallery(bitmap, name)
         //save on server
         if (uri != null) {
             savetoserver(name,uri)
         }
     }

    private suspend fun updateimages(storageReference:StorageReference,name:String) {
            val imageuri=storageReference.downloadUrl.await()
            val name=storageReference.name
       val uid=firebaseAuth.currentUser?.uid
        val userref= uid?.let { firestore.collection("users").document(it) }
        val user = userref?.get()?.await()?.toObject(user::class.java)
        val image=image(name,imageuri.toString(), Date())
        if (user != null) {
            user.images.add(image)
            userref.update("images", user.images).await()
        }
    }

    private fun saveImageToGallery(bitmapImage: Bitmap, title: String):Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, title)
            put(MediaStore.Images.Media.TITLE, title)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        // Insert the metadata into the MediaStore
        val uri = application.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        // Use an OutputStream to save the bitmap to the MediaStore
        uri?.let {
            application.contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
            }
        }
        return uri
    }
    private suspend fun savetoserver(name: String, imageUri:Uri){
        val storageReference = storage.getReference("images/$name")
        val y= storageReference.putFile(imageUri)
             .addOnSuccessListener {
                 Toast.makeText(application, "Successfully Uploaded", Toast.LENGTH_SHORT).show()
             }
             .addOnFailureListener {
                 Toast.makeText(application, "Failed to Upload", Toast.LENGTH_SHORT).show()
             }.await()
        if(y.task.isSuccessful) {
            updateimages(storageReference, name)
        }
    }
     suspend fun load_meme(): ArrayList<image> {
         val user = userref?.get()?.await()?.toObject(user::class.java)
         if (user != null) {
             return user.images
         }
         return ArrayList()
    }

    suspend fun delete_item(index:Int) {
        val name=""
        if (userref != null) {
                    val user = userref?.get()?.await()?.toObject(user::class.java)
                    val name= user?.images?.get(index)?.name
                    user?.images?.removeAt(index)
                    storage.getReference("images/$name").delete()
                    userref.update("images", user?.images).await()
        }
    }
    suspend fun downloadImage(index:Int) {
            try {
                val user = userref?.get()?.await()?.toObject(user::class.java)
                val imageUrl= user?.images?.get(index)?.url
                val title= user?.images?.get(index)?.name

                val inputStream =
                    withContext(Dispatchers.IO) {
                        URL(imageUrl).openStream()
                    }
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (title != null) {
                    saveImageToGallery(bitmap,title)
                }
                // Image downloaded successfully
                withContext(Dispatchers.Main) {
                    Toast.makeText(application, "Image downloaded", Toast.LENGTH_SHORT).show()
                }
            } catch (exception: IOException) {
                // Error occurred while downloading the image

                withContext(Dispatchers.Main) {
                    Toast.makeText(application, "Failed to download image: ${exception.message}", Toast.LENGTH_SHORT).show()                }
            }

    }


}