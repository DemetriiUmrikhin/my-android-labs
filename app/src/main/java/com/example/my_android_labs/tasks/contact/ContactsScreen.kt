package com.example.my_android_labs.tasks.contact

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.my_android_labs.navigation.NavigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onBack: () -> Unit,  // Добавляем параметр для навигации назад
    navViewModel: NavigationViewModel? = null  // Делаем необязательным
) {
    val context = LocalContext.current
    val contacts = remember { generateSampleContacts() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Контакты") },
                        navigationIcon = {
                    IconButton(onClick = onBack) {  // Добавляем кнопку назад
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(contacts) { contact ->
                ContactItem(
                    contact = contact,
                    onCallClick = { makeCall(context, contact.phone) },
                    onLongClick = { shareContact(context, contact) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ContactItem(
    contact: Contact,
    onCallClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCallClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Контакт",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = contact.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Телефон",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = contact.phone,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = contact.email,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onLongClick,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Поделиться"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Отправить контакт")
            }
        }
    }
}

// Генерация тестовых данных
fun generateSampleContacts(): List<Contact> {
    return listOf(
        Contact(1, "Иван Петров", "+79161234567", "ivan@example.com"),
        Contact(2, "Мария Сидорова", "+79035551234", "maria@example.com"),
        Contact(3, "Алексей Иванов", "+79266543210", "alex@example.com"),
        Contact(4, "Екатерина Смирнова", "+79998887766", "ekaterina@example.com"),
        Contact(5, "Дмитрий Кузнецов", "+79031112233", "dmitry@example.com")
    )
}

// Совершение звонка
fun makeCall(context: Context, phone: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phone")
    }
    context.startActivity(intent)
}

// Отправка контакта по почте
fun shareContact(context: Context, contact: Contact) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Контакт: ${contact.name}")
        putExtra(Intent.EXTRA_TEXT,
            "Имя: ${contact.name}\nТелефон: ${contact.phone}\nEmail: ${contact.email}")
    }
    context.startActivity(Intent.createChooser(intent, "Поделиться контактом"))
}