package com.example.final_project_jetpack

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun UserList(
    users: List<MainActivity.User>,
    removeUser: (MainActivity.User) -> Unit,
    updateUser: (MainActivity.User, String, String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LazyColumn {
            items(users) { user ->
                UserClick(
                    user = user,
                    removeUser = removeUser,
                    updateUser = updateUser)
            }
        }
    }
}

@Composable
fun UserClick(
    user: MainActivity.User,
    removeUser: (MainActivity.User) -> Unit,
    updateUser: (MainActivity.User, String, String) -> Unit) {

    var options by remember { mutableStateOf(false) }
    var updateDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }
    var changeFirstName by remember { mutableStateOf("") }
    var changeLastName by remember { mutableStateOf("") }


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
                DropdownMenuItem(onClick = { updateDialog = true }) {
                    Text(text = "Update User")
                }
                DropdownMenuItem(onClick = { deleteDialog = true }) {
                    Text(text = "Remove User")
                }
            }
        }
    }
    if (updateDialog) {
        Dialog(
            onDismissRequest = { updateDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Updating ${user.firstName} ${user.lastName}",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    TextField(
                        value = changeFirstName,
                        onValueChange = { changeFirstName = it },
                        label = { Text(text = "First Name") }
                    )
                    TextField(
                        value = changeLastName,
                        onValueChange = { changeLastName = it },
                        label = { Text(text = "Last Name") }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) { Button(
                        onClick = {
                            if (changeFirstName.isNotBlank()&&
                                changeLastName.isNotBlank()){

                                updateDialog = false
                                options = false
                                updateUser(
                                    user,
                                    changeFirstName,
                                    changeLastName)
                            }
                        }
                    ) {
                        Text(text = "Update")
                    }
                        Button(
                            onClick = {
                                updateDialog = false
                                options = false
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }
    if (deleteDialog) {
        Dialog(
            onDismissRequest = { deleteDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Do you want to delete user:",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(text = "${user.firstName} ${user.lastName}")
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ){ Button(
                        onClick = { deleteDialog = false
                                    options = false
                                    removeUser(user)}
                    ) {
                        Text(text = "Yes")
                    }
                        Button(
                            onClick = {
                                deleteDialog = false
                                options = false
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }
}