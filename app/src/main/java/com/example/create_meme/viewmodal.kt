package com.example.create_meme

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.create_meme.models.image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class viewmodal(application: Application = Application()): ViewModel() {
    private val repository=repository(application)
    suspend fun fetchmeme(): ArrayList<image> {
        var x=GlobalScope.async(Dispatchers.IO) {
          repository.load_meme()!!
        }
        return x.await()
    }
}