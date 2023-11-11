package com.example.feedfindr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipesAdapter (private val imageList: List<String>, private val nameList: List<String>): RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById<ImageView>(R.id.recipeImage)
        val name = itemView.findViewById<TextView>(R.id.recipeName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_recipe, parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = nameList[position]

        Glide.with(holder.itemView)
            .load(imageList[position])
            .centerCrop()
            .into(holder.image)

    }
}