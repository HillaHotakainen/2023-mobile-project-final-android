package com.example.final_project_jetpack

import android.widget.Toast
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

    Row {
        Text(
            text = "${user.firstName} ${user.lastName}",
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        Box {
            // Options button
            IconButton(
                onClick = { options = true },
                modifier = Modifier.padding(6.dp)
            ){
                Icon(Icons.Default.MoreVert, contentDescription = "Options")
            }
            DropdownMenu(
                expanded = options,
                onDismissRequest = { options = false }
            ) {
                DropdownMenuItem(onClick = { /* Handle update user click */ }) {
                    Text(text = "Update User")
                }
                DropdownMenuItem(onClick = { /* Handle remove user click */ }) {
                    Text(text = "Remove User")
                }
            }
        }

    }
}