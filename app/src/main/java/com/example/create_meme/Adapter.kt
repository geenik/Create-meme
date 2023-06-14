package com.example.create_meme

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.create_meme.models.image
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class Adapter(context: Context): RecyclerView.Adapter<AdapterViewholder>(){
    private val adaptercallback=context as adaptercallback
    private var items=ArrayList<image>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.element_view,parent,false)
        return AdapterViewholder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewholder, position: Int) {
        val currentitem=items[position]
        holder.title.text=currentitem.name
        Glide.with(holder.itemView.context).load(currentitem.url).into(holder.imageview)
        var date=utils.covertTimeToText(currentitem.date.toString())
        holder.date.text=date
        holder.delete.setOnClickListener {
             adaptercallback.delete_item(position)
        }
        holder.dowload_btn.setOnClickListener {
            adaptercallback.download(position)
        }
    }

    override fun getItemCount(): Int {
      return items.size
    }
    fun update_data(item:ArrayList<image>){
        items.clear()
        items.addAll(item)
        notifyDataSetChanged()
    }


}


class AdapterViewholder(itemView: View):RecyclerView.ViewHolder(itemView) {
   val imageview=itemView.findViewById<ImageView>(R.id.image)
    val title=itemView.findViewById<TextView>(R.id.name)
    val date=itemView.findViewById<TextView>(R.id.date)
    val delete=itemView.findViewById<AppCompatImageButton>(R.id.delete_btn)
    val dowload_btn=itemView.findViewById<AppCompatImageButton>(R.id.download)
}
interface adaptercallback {
    fun delete_item(index:Int)
    fun download(index: Int)
}