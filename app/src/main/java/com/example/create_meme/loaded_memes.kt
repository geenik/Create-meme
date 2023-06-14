package com.example.create_meme

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.create_meme.models.image
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class loaded_memes : AppCompatActivity(),adaptercallback {
    lateinit var viewmodel:viewmodal
    lateinit var madapter:Adapter
    lateinit var progressBar: ProgressBar
    lateinit var floatingbtn:FloatingActionButton
    lateinit var repo:repository
    lateinit var nodata: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loaded_memes)
        floatingbtn=findViewById(R.id.fab)
        progressBar=findViewById(R.id.progressBar)
        nodata=findViewById(R.id.nodata)
        viewmodel= ViewModelProvider(this)[viewmodal::class.java]
        val recyclerView=findViewById<RecyclerView>(R.id.recycleview)
        recyclerView.layoutManager= LinearLayoutManager(this)
        madapter= Adapter(this)
        repo= repository(application)
        recyclerView.adapter=madapter
       GlobalScope.launch(Dispatchers.Main) {
           load_data()
       }
        floatingbtn.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }
    suspend fun load_data(){
        progressBar.visibility=VISIBLE
        var x=ArrayList<image>()
        GlobalScope.async {
           x =viewmodel.fetchmeme()
        }.await()
        if(x.isEmpty()){
            nodata.visibility= VISIBLE
        }else nodata.visibility= GONE
        madapter.update_data(x)
        progressBar.visibility= GONE
    }

    override fun delete_item(index: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            repo.delete_item(index)
            load_data()
        }
    }

    override fun download(index: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            repo.downloadImage(index)
        }
    }


}