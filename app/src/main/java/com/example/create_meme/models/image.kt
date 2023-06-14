package com.example.create_meme.models
import android.net.Uri
import java.util.*

data class image(
    var name:String,
    var url: String,
    var date: Date
){
    constructor() : this("", "", Date())
}
