package com.example.final_project_jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.final_project_jetpack.ui.theme.FinalprojectjetpackTheme
import org.json.JSONArray
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : ComponentActivity() {
    private val users = mutableStateListOf<User>()
    private var searchUsers by mutableStateOf("")
    private var isLoading by mutableStateOf(true)
    private var showDialog by mutableStateOf(false)

    data class User(
        val firstName: String,
        val lastName: String,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchUsers()
        setContent {
            FinalprojectjetpackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        SearchBar(onSearch = { query -> searchUsers = query })
                        Button(
                            onClick = { showDialog = true }
                        ) {
                            Text(text = "Add User")
                        }
                        if (showDialog) {
                            AddUserDialog(
                                onAddUser = { user ->
                                    addUser(user)
                                    showDialog = false
                                },
                                onCancel = { showDialog = false }
                            )
                        }
                        Box {
                            if (isLoading) {
                                CircularProgressIndicator()
                            } else {
                                val filteredUsers =
                                    filterUsers(users,searchUsers)
                                UserList(users = filteredUsers)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fetchUsers() {
        val url = "https://dummyjson.com/users"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null, { response ->
                val usersJArray: JSONArray = response.getJSONArray("users")
                for (i in 0 until usersJArray.length()) {
                    val userJObject: JSONObject = usersJArray.getJSONObject(i)
                    val user = User(
                        firstName = userJObject.getString("firstName"),
                        lastName = userJObject.getString("lastName"),
                    )
                    users.add(user)
                }
                isLoading = false
            },
            { error ->
                isLoading = false
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun addUser(user: MainActivity.User) {
        val url = "https://dummyjson.com/users/add"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObject = JSONObject()
        jsonObject.put("firstName", user.firstName)
        jsonObject.put("lastName", user.lastName)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                val newUser = MainActivity.User(
                    firstName = response.getString("firstName"),
                    lastName = response.getString("lastName")
                )
                users.add(newUser)
                showDialog = false
            },
            { error ->
                // Handle error
                showDialog = false
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun filterUsers(users: List<User>, query: String): List<User> {
        return if (query.isEmpty()) {
            users
        } else {
            users.filter { user ->
                user.firstName.startsWith(query, ignoreCase = true) ||
                        user.lastName.startsWith(query, ignoreCase = true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        users.clear()
    }
}
