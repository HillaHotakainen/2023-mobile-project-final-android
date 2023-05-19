package com.example.final_project_jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserList(users: List<MainActivity.User>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LazyColumn {
            items(users) { user ->
                UserClick(user = user)
            }
        }
    }
}

@Composable
fun UserClick(user: MainActivity.User) {
    var options by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { options = !options }
            .padding(16.dp)
    ) {
        Text(
            text = "${user.firstName} ${user.lastName}",
            modifier = Modifier.weight(1f)
        )
        //Options button
        IconButton(
            onClick = { options = !options }
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "Options")
        }
    }

    //open options
    if (options) {
        UserOptions(user = user)
    }
}

@Composable
fun UserOptions(user: MainActivity.User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 64.dp, end = 16.dp)
    ) {
        //Update user
        Text(
            text = "Update User",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* update user thingy here */ }
                .padding(vertical = 8.dp)
        )
        //Remove user
        Text(
            text = "Remove User",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* remove user thingy here */ }
                .padding(vertical = 8.dp)
        )
    }
}