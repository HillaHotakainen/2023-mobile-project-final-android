package com.example.final_project_jetpack

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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
    private var searchUsers by mutableStateOf("")

    data class User(
        val firstName: String,
        val lastName: String,
    )
    private var isLoading by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchUsers()
        setContent {
            FinalprojectjetpackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val filteredUsers = filterUsers(users, searchUsers)
                    Column {
                        SearchBar(onSearch = { query -> searchUsers = query })

                        LaunchedEffect(Unit) {
                            fetchUsers()
                        }
                        if (isLoading) {
                            CircularProgressIndicator()
                        } else {
                            UserList(users = filteredUsers)
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
                isLoading = false
            },
            { error ->
                isLoading = false
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun filterUsers(users: List<User>, query: String): List<User> {
        return if (query.isEmpty()) {
            users
        } else {
            users.filter { user ->
                user.firstName.contains(query, ignoreCase = true) ||
                        user.lastName.contains(query, ignoreCase = true)
            }
        }
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

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        label = { Text("Search Users") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
                query = ""
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier.fillMaxWidth()
    )
}