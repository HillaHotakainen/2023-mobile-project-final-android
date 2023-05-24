package com.example.final_project_jetpack

import android.os.Bundle
import android.widget.Toast
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
        val id : String,
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
                                UserList(
                                    users = filteredUsers,
                                    removeUser = { user -> removeUser(user)},
                                    updateUser = {
                                            user,
                                            newFirstName,
                                            newLastName ->
                                            updateUser(
                                                user,
                                                newFirstName,
                                                newLastName)})
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
                        id = userJObject.getString("id"),
                        firstName = userJObject.getString("firstName"),
                        lastName = userJObject.getString("lastName"),
                    )
                    users.add(user)
                }
                isLoading = false
            },
            { error ->
                isLoading = false
                val errorMessage = "Error: ${error.message}"
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun addUser(user: User) {
        val url = "https://dummyjson.com/users/add"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObject = JSONObject()
        jsonObject.put("id", (users.size + 1).toString())
        jsonObject.put("firstName", user.firstName)
        jsonObject.put("lastName", user.lastName)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                val newUser = User(
                    id = response.getString("id"),
                    firstName = response.getString("firstName"),
                    lastName = response.getString("lastName")
                )
                users.add(newUser)
                showDialog = false
            },
            { error ->
                showDialog = false
                val errorMessage = "Error: ${error.message}"
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
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

    private fun removeUser(user: User) {
        users.remove(user)
    }

    private fun updateUser(
        user: User,
        newFirstName: String,
        newLastName: String) {
        if (user.id.toInt() > 100) {
            val updatedUser = user.copy(
                firstName = newFirstName,
                lastName = newLastName
            )
            users[users.indexOf(user)] = updatedUser
        } else {
            val url = "https://dummyjson.com/users/${user.id}"
            val requestQueue = Volley.newRequestQueue(this)

            val jsonObject = JSONObject()
            jsonObject.put("firstName", newFirstName)
            jsonObject.put("lastName", newLastName)

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.PATCH, url, jsonObject,
                { response ->
                    val updatedUser = User(
                        id = response.getString("id"),
                        firstName = response.getString("firstName"),
                        lastName = response.getString("lastName")
                    )
                    users[users.indexOf(user)] = updatedUser
                },
                { error ->
                    val errorMessage = "Error: ${error.message}"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            )
            requestQueue.add(jsonObjectRequest)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        users.clear()
    }
}