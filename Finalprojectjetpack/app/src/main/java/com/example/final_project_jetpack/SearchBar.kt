package com.example.final_project_jetpack

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

/**
 * Composable function that displays a search bar for filtering users.
 *
 * @param onSearch The callback function invoked when the search query changes.
 */

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    //if back button is pressed clears focus
    BackHandler(enabled = true) {
        focusManager.clearFocus()
    }

    OutlinedTextField(
        value = query,
        //calls onSearch everytime value changes
        onValueChange = { query = it
                            onSearch(query)
                        },
        label = { Text("Search Users") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            //if search icon is pressed clears focus
            onSearch = {
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}