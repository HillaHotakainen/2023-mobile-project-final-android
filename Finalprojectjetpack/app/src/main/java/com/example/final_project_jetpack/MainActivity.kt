package com.example.final_project_jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
    private val users = mutableStateListOf<User>()
    private var isLoading = mutableStateOf(false)

    data class User(
        val firstName: String,
        val lastName: String,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalprojectjetpackTheme {
                Surface(color = MaterialTheme.colors.background) {
                    UserList(users = users, isLoading = isLoading.value, fetchUsers = this::fetchUsers)
                }
            }
        }
    }

    private fun fetchUsers() {
        isLoading.value = true
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
                isLoading.value = false
            },
            { error ->
                // Handle error
                isLoading.value = false
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
fun UserList(users: List<MainActivity.User>, isLoading: Boolean, fetchUsers: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { fetchUsers() },
            enabled = !isLoading && users.isEmpty() // Disable the button if users are already fetched or currently loading
        ) {
            Text(text = "Fetch Users")
        }
        if (isLoading) {
            Text(text = "Loading...")
        } else {
            if (users.isNotEmpty()) {
                Surface(modifier = Modifier.weight(1f)) {
                    LazyColumn {
                        items(users) { user ->
                            Text(text = "${user.firstName} ${user.lastName}")
                        }
                    }
                }
            } else {
                Text(text = "No users available")
            }
        }
    }
}