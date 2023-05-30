package com.example.final_project_jetpack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Composable for adding a new user.
 *
 * @param addUser Callback when new user is added.
 * @param onCancel Callback when adding new user is cancelled.
 */

@Composable
fun AddUserDialog(
    addUser: (MainActivity.User) -> Unit,
    onCancel: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onCancel,
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
                    text = "Add User",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text(text = "First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text(text = "Last Name") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            //Stops user from adding empty string as user
                            if (firstName.isNotBlank()&&lastName.isNotBlank()) {
                                val newUser = MainActivity.User(
                                    id = "",
                                    firstName = firstName,
                                    lastName = lastName)
                                //callback of addUser with new user
                                addUser(newUser)
                            }
                        },
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Text(text = "Add")
                    }
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}
