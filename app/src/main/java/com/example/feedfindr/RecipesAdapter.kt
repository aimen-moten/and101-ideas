package com.example.feedfindr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


/*Updated the class constructor to accept ingredientList as a Map. */
class RecipesAdapter (private val imageList: List<String>, private val nameList: List<String>,
                      private val ingredientList: Map<String, String>): RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById<ImageView>(R.id.recipeImage)
        val name = itemView.findViewById<TextView>(R.id.recipeName)
        /*Added new textview to store ingredients text for each entry. */
        val ingredients = itemView.findViewById<TextView>(R.id.RecipeIngredients)

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
        val recipeName = nameList[position]// ?: "Name not available"
        holder.name.text = nameList[position]
        /*Set the ingredient text in the holder to whatever is in the list. */
        holder.ingredients.text = ingredientList[recipeName] //?: "Ingredients not available"
        Glide.with(holder.itemView)
            .load(imageList[position])
            .centerCrop()
            .into(holder.image)

    }
}