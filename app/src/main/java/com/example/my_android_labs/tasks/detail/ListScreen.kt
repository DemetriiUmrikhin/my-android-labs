package com.example.my_android_labs.tasks.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*

import androidx.compose.ui.unit.dp

@Composable
fun ListScreen(
    viewModel: ListViewModel,
    onItemSelected: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedItemId = viewModel.selectedItem.value?.id

    LazyColumn(modifier = modifier) {
        items(viewModel.items) { item ->
            ListItem(
                item = item,
                isSelected = item.id == selectedItemId,
                onItemSelected = {
                    viewModel.selectItem(item)
                    onItemSelected(item)
                }
            )
        }
    }
}

@Composable
fun ListItem(
    item: Item,
    isSelected: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onItemSelected),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = item.title,
            style = MaterialTheme.typography.titleMedium,

        )
    }
}