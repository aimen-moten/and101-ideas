package com.example.feedfindr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.util.Random

class MainActivity : AppCompatActivity() {

    private var query = ""
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initializing instance variables
        recyclerView = findViewById(R.id.results)
        var input = findViewById<EditText>(R.id.query)
        query = input.toString()

        /* First we set the API url. Note, it requires the apiKey set at the top! */
        val apiUrl = "https://api.spoonacular.com/recipes/complexSearch?apiKey=062d27ad37e442a7bcca3f349b183338&query=apple&maxFat=25&number=4"
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
                Log.d("PRINTING RESULT ARRAY", resultsArray.toString())

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