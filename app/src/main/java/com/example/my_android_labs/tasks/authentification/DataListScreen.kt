package com.example.my_android_labs.tasks.authentification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.my_android_labs.navigation.Screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

@Composable
fun DataListScreen(
    dbType: String,
    navController: NavHostController,
    viewModel: DataViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(dbType) {
        viewModel.init(dbType)
        isLoading = false
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.ItemEdit.createRoute("NEW", dbType))
                }
            ) { Icon(Icons.Filled.Add, "Add") }
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (viewModel.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No items found")
            }
        } else {
            LazyColumn(Modifier.padding(padding)) {
                items(viewModel.items) { item ->
                    ListItem(
                        headlineContent = {
                            Column {
                                Text(item.title)
                                Text(item.content, style = MaterialTheme.typography.bodySmall)
                            }
                        },
                        modifier = Modifier.clickable {
                            item.id?.let { id ->
                                navController.navigate(Screen.ItemEdit.createRoute(id, dbType))
                            }
                        }
                    )
                }
            }
        }
    }
}