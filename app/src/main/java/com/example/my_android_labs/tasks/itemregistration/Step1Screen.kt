package com.example.my_android_labs.tasks.itemregistration

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun Step1Screen(
    viewModel: ItemViewModel,
    onNext: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Общий контейнер с отступами и скроллом
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val isSmallScreen = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Шаг 1/3: Основные данные", style = MaterialTheme.typography.titleLarge)

            // Разная компоновка в зависимости от ориентации и размера экрана
            if (isPortrait || isSmallScreen) {
                // Портретная ориентация или маленький экран
                PortraitLayout(viewModel)
            } else {
                // Альбомная ориентация на достаточно широком экране
                LandscapeLayout(viewModel)
            }

            // Кнопка "Далее" - общая для всех вариантов
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.name.isNotBlank() && viewModel.version.isNotBlank()
            ) {
                Text("Далее")
            }
        }
    }
}

@Composable
private fun PortraitLayout(viewModel: ItemViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AppNameField(viewModel)
        VersionField(viewModel)
        LicenseSelector(viewModel)
        InAppPurchasesCheckbox(viewModel)
    }
}

@Composable
private fun LandscapeLayout(viewModel: ItemViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppNameField(viewModel)
            VersionField(viewModel)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LicenseSelector(viewModel)
            InAppPurchasesCheckbox(viewModel)
        }
    }
}

// Общие компоненты (без изменений)
@Composable
fun AppNameField(viewModel: ItemViewModel) {
    OutlinedTextField(
        value = viewModel.name,
        onValueChange = { viewModel.name = it },
        label = { Text("Название приложения") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun VersionField(viewModel: ItemViewModel) {
    OutlinedTextField(
        value = viewModel.version,
        onValueChange = { viewModel.version = it },
        label = { Text("Версия") },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseSelector(viewModel: ItemViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val licenses = listOf("Free", "Paid", "Subscription", "Open Source")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = viewModel.licenseType,
            onValueChange = {},
            label = { Text("Тип лицензии") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            licenses.forEach { license ->
                DropdownMenuItem(
                    text = { Text(license) },
                    onClick = {
                        viewModel.licenseType = license
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun InAppPurchasesCheckbox(viewModel: ItemViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = viewModel.hasInAppPurchases,
            onCheckedChange = { viewModel.hasInAppPurchases = it }
        )
        Text("Есть встроенные покупки")
    }
}