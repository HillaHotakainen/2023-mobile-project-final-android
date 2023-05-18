package com.example.final_project_jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.final_project_jetpack.ui.theme.FinalprojectjetpackTheme
import org.json.JSONArray
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : ComponentActivity() {
    private val users = mutableListOf<User>()

    data class User(
        val firstName: String,
        val lastName: String,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchUsers()
    }

    private fun fetchUsers() {
        val url = "https://dummyjson.com/users"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val usersJsonArray: JSONArray = response.getJSONArray("users")
                for (i in 0 until usersJsonArray.length()) {
                    val userJsonObject: JSONObject = usersJsonArray.getJSONObject(i)
                    val user = User(
                        firstName = userJsonObject.getString("firstName"),
                        lastName = userJsonObject.getString("lastName"),
                    )
                    users.add(user)
                }
                setContent {
                    UserList(users = users)
                }
            },
            { error ->
                // Handle error
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        users.clear()
    }
}

@Composable
fun UserList(users: List<MainActivity.User>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LazyColumn {
            items(users) { user ->
                Text(text = "${user.firstName} ${user.lastName}")
            }
        }
    }
}