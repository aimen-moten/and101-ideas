package com.example.feedfindr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.util.Random

class MainActivity : AppCompatActivity() {

    private var recipeURL = mutableListOf<String>()
    private var recipeName = mutableListOf<String>()
    /* I'm adding a hashMap called recipeIngredients.
    *  As you probably guessed, I'm mapping the recipe ingredients to the recipe names!
    * */
    private var recipeIngredients = hashMapOf<String, String>()
    private var query = ""
    private lateinit var recyclerView : RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing instance variables
        recyclerView = findViewById(R.id.results)

        var input = findViewById<EditText>(R.id.query)
        var maxFat = findViewById<EditText>(R.id.maxfat)
        var maxCalories = findViewById<EditText>(R.id.maxcalories)
        var maxProtein = findViewById<EditText>(R.id.maxprotein)

        /* When you focus for the first time, remove the text. */
        input.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                input.text.clear()
            }
        }
        var button = findViewById<Button>(R.id.serachBtn)


        button.setOnClickListener {
            query = input.text.toString()
            if(maxFat.text != null || !maxFat.text.equals(" ")){
                query.plus("&maxFat=${maxFat.text}")
            }
            if(maxProtein.text != null || !maxProtein.text.equals(" ")){
                query.plus("&maxProtein=${maxProtein.text}")
            }
            if(maxCalories.text != null || !maxCalories.text.equals(" ")){
                query.plus("&maxCalories=${maxProtein.text}")
            }
            fetchAPI()
        }

    }

        private fun fetchAPI() {
            /* First we set the API url. Note, it requires the apiKey set at the top! */
            val apiUrl = "https://api.spoonacular.com/recipes/complexSearch?apiKey=062d27ad37e442a7bcca3f349b183338&query=${query}&number=4"
            val client = AsyncHttpClient()
            /* for codepath client library, the headers are just are Hashmaps.*/
            val headers = RequestHeaders()

            /*Set the map to this value. */
            headers["Content-Type"] = "application/json"

            /* This version of the get command includes the header.
            *  I have temporarily set params to null. We'll need to create a params
            *  hashmap so that we can add queries and what not to the url.
            * */
            client.get(apiUrl, headers, null, object: JsonHttpResponseHandler(){

                override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                    val jsonObject = json.jsonObject
                    val resultsArray = jsonObject.getJSONArray("results")

                    recipeURL.clear();
                    recipeName.clear();
                    recipeIngredients.clear();

                    //loop through the array
                    for (i in 0 until resultsArray.length()) {
                        val url = resultsArray.getJSONObject(i).getString("image")
                        val name = resultsArray.getJSONObject(i).getString("title")
                        /* Recipe ID is needed so that we can run another GET request using the API and get a list of ingredients. */
                        val recipe_id = resultsArray.getJSONObject(i).getString("id")

                        /* This is the URL that we will use. */
                        val ingredient_url = "https://api.spoonacular.com/recipes/${recipe_id}/information?apiKey=062d27ad37e442a7bcca3f349b183338"
                        recipeURL.add(url)
                        recipeName.add(name)

                        /* Making new header, since I'm going to make a separate HTTP GET request to get the ingredients. */
                        val new_headers = RequestHeaders()
                        new_headers["Content-Type"] = "application/json"
                        client.get(ingredient_url, new_headers, null, object : JsonHttpResponseHandler() {
                            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {

                                val detailObject = json.jsonObject
                                /* In this API, the ingredients are listed under the extendedIngredients as an array.*/
                                val extendedIngredients = detailObject.getJSONArray("extendedIngredients")

                                val ingredientsList = mutableListOf<String>()
                                /* We parse the JSON array to fill a local variable called ingredientsList. */
                                for (j in 0 until extendedIngredients.length()) {
                                    val ingredient = extendedIngredients.getJSONObject(j)
                                    val ingredientName = ingredient.getString("name")
                                    ingredientsList.add(ingredientName)
                                }
                                /* Next we add the list to a hashmap with a key value set the same as the recipe name. */
                                recipeIngredients[name] = ingredientsList.joinToString(", ")

                            }

                            override fun onFailure(
                                statusCode: Int,
                                headers: Headers?,
                                errorResponse: String,
                                throwable: Throwable?
                            ) {
                                Log.d("ERROR TEXT", errorResponse)
                            }
                        })

                    }

                    /* Modified the adapter to take a Hashmap as a variable. */
                    val adapter = RecipesAdapter(recipeURL, recipeName, recipeIngredients)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                    Log.d("PRINTING RESULT ARRAY", resultsArray.toString())
                    Log.d("PRINTING RESULT ARRAY", recipeURL.toString())
                    Log.d("Printing name array", recipeName.toString())
                    Log.d("Printing Ingredients!", recipeIngredients.toString())

                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    throwable: Throwable?
                ) {
                    Log.d("ERROR TEXT", errorResponse)
                }
            })
        }
    }