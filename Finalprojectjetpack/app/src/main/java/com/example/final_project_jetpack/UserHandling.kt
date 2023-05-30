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

/**
 * Composable that displays a list of users.
 *
 * @param users The list of users to display.
 * @param removeUser The callback function invoked when a user is to be removed.
 * @param updateUser The callback function invoked when a user is to be updated.
 */

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

/**
 * Composable that displays a user item and handles user interactions.
 *
 * @param user The user to display.
 * @param removeUser The callback when user is removed.
 * @param updateUser The callback when user is updated.
 */

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
                //dropdownmenu for updating and deleting user
                //is shown if options icon is pressed
                expanded = options,
                onDismissRequest = { options = false }
            ) {
                //opens update dialog on press
                DropdownMenuItem(onClick = { updateDialog = true }) {
                    Text(text = "Update User")
                }
                //opens delete dialog on press
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
                        //shows the name of currently opened user
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
                            //checks that name is not empty
                            if (changeFirstName.isNotBlank() &&
                                changeLastName.isNotBlank()) {

                                updateDialog = false
                                options = false
                                //updateUser callback
                                updateUser(
                                    user,
                                    changeFirstName,
                                    changeLastName)
                            }
                        },
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Text(text = "Update")
                    }
                        Button(
                            onClick = {
                                updateDialog = false
                                options = false
                            },
                            modifier = Modifier.padding(6.dp)
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
                    //shows name of user you are deleting
                    Text(text = "${user.firstName} ${user.lastName}")
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ){ Button(
                        //callback removeUser when pressed yes
                        onClick = { deleteDialog = false
                                    options = false
                                    removeUser(user)},
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Text(text = "Yes")
                    }
                        Button(
                            onClick = {
                                deleteDialog = false
                                options = false
                            },
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }
}