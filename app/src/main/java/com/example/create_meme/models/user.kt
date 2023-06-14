package com.example.create_meme.models

data class user(
    var email:String,
    var images: ArrayList<image> =ArrayList()
){
    constructor() : this("",ArrayList())
}

