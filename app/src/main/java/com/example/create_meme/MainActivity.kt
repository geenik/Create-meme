package com.example.create_meme

import android.Manifest.permission.*
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity()  {
    lateinit var progressBar: ProgressBar
    private val REQUEST_PERMISSIONS = 1
    lateinit var scaleGestureDetector: ScaleGestureDetector
    lateinit var Btn:AppCompatButton
    lateinit var textbtn:AppCompatButton
    lateinit var savebtn:AppCompatButton
    lateinit var text:EditText
    private var scaleFactor = 1.0f
    lateinit var parentview:LinearLayout
    lateinit var repo:repository
   //lateinit var imageView :ImageView
   lateinit var dragView:DraggableCoordinatorLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar=findViewById(R.id.progressBar)
        dragView=findViewById(R.id.dragview)
        Btn = findViewById(R.id.image_btn)
        text=findViewById(R.id.edittext)
        textbtn=findViewById(R.id.text_btn)
        parentview=findViewById(R.id.parentview)
        savebtn=findViewById(R.id.save_btn)
        repo= repository(application)
        //dragView= DraggableCoordinatorLayout(this)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        requestpermisson()
        Btn.setOnClickListener {
            chooseImage(this);
        }
        savebtn.setOnClickListener{
            take_name()
        }
// Check for permission status and request permission if not granted

        textbtn.setOnClickListener{
            val txt=text.text.toString()
            if(txt.isNotEmpty()){
                val textview=TextView(this)
                textview.text = txt
                textview.isClickable=true
                textview.textSize= 30F
                textview.setTypeface(null, Typeface.BOLD)
                textview.isFocusable=true
                textview.isFocusableInTouchMode=true
                dragView.addView(textview)
                dragView.draggableChildren.add(textview)
                textview.setTextColor(Color.BLACK)
                text.text.clear()
                text.clearFocus()

            }
        }
        currentFocus?.setOnLongClickListener {
            currentFocus!!.setBackgroundResource(R.drawable.border)
            return@setOnLongClickListener true
        }

    }

    private fun deleteview() {
        Log.d(TAG, currentFocus.toString())
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector?.onTouchEvent(event)
        return true
    }


    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            var view=currentFocus
            if (view != null&&view!=parentview ) {
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            }
            return true
        }
    }



    private fun requestpermisson(){
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE,
                    CAMERA),
                REQUEST_PERMISSIONS)
        }
    }
    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        ) // create a menuOption Array
        // create a dialog for showing the optionsMenu
        val builder: AlertDialog.Builder= AlertDialog.Builder(context)
        // set the items in builder
        builder.setItems(optionsMenu
        ) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {
                // Open the camera and get the photo
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (optionsMenu[i] == "Choose from Gallery") {
                // choose from  external storage
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            } else if (optionsMenu[i] == "Exit") {
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do your work
            } else {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.extras!!["data"] as Bitmap?
                    val imageview = ImageView(this)
                    imageview.setImageBitmap(selectedImage)
                    imageview.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    imageview.layoutParams.height=400
                    imageview.layoutParams.width=400
                    imageview.scaleType = ImageView.ScaleType.FIT_XY
                    imageview.isClickable=true
                    imageview.isFocusable=true
                    imageview.isFocusableInTouchMode=true
                    dragView.draggableChildren.add(imageview)
                    dragView.addView(imageview)

                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage: Uri? = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor: Cursor? =
                            contentResolver.query(selectedImage, filePathColumn, null, null, null)
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath: String = cursor.getString(columnIndex)
                            val imageview = ImageView(this)
                            imageview.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                            imageview.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            imageview.layoutParams.height=400
                            imageview.layoutParams.width=400
                            imageview.scaleType = ImageView.ScaleType.FIT_XY
                            imageview.isClickable=true
                            imageview.isFocusable=true
                            imageview.isFocusableInTouchMode=true
                            dragView.draggableChildren.add(imageview)
                            dragView.addView(imageview)
                            cursor.close()
                        }
                    }
                }
            }
        }
    }

   private fun take_name(){
       //     create bitmap
       val bitmap = Bitmap.createBitmap(dragView.width, dragView.height, Bitmap.Config.ARGB_8888)
       val canvas = Canvas(bitmap)
       canvas.drawColor(Color.WHITE)
       dragView.draw(canvas)
       val builder = AlertDialog.Builder(this)
       builder.setTitle("Enter Your Image Name")
           .setMessage("Please enter Image name below:")
       val input = EditText(this)
       builder.setView(input)
       builder.setPositiveButton("OK") { _, _ ->
           val name = input.text.toString()
           GlobalScope.launch(Dispatchers.Main){
               progressBar.visibility=VISIBLE
               uploadimage(bitmap, name)
               progressBar.visibility= GONE
           }
       }
       builder.setNegativeButton("Cancel") { dialog, _ ->
           dialog.cancel()
       }

       val dialog = builder.create()
       dialog.show()
   }

    private suspend fun uploadimage(bitmap: Bitmap, name: String) {
        val wrk=GlobalScope.async(Dispatchers.Main)
        {
            repo.uploadImage(bitmap, name)
        }
        wrk.await()
        val intent=Intent(this,loaded_memes::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

}