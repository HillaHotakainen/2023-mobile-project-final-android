package com.example.final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Button
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private lateinit var lv: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lv = findViewById<ListView>(R.id.listView)
        adapter = ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    mutableListOf<String>())
        lv.adapter = adapter

        val fetchButton: Button = findViewById(R.id.fetchButton)
        fetchButton.setOnClickListener {
            fetchData()
        }
    }

    private fun fetchData() {
        val url = "https://dummyjson.com/users"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val usersArray = response.getJSONArray("users")
                val persons: MutableList<String> = mutableListOf()

                for (i in 0 until usersArray.length()) {
                    val userObject = usersArray.getJSONObject(i)
                    val name = userObject.getString("firstName") + " " +
                                userObject.getString("lastName")
                    persons.add(name)
                }

                adapter.addAll(persons)
            },
            { error ->
                error.printStackTrace()
            })

        Volley.newRequestQueue(this).add(request)
    }
}