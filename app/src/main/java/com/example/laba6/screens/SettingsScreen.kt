package com.example.laba6.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.laba6.data.TimedDatabaseRequestController

@Composable
fun SettingsScreen(
    darkMode: Boolean,
    onThemeChange: (Boolean) -> Unit,
    databaseDelayEnabled: Boolean,
    onDatabaseDelayChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {

    val requestController = remember { TimedDatabaseRequestController() }
    val requestState by requestController.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        // Заголовок экрана
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Настройка темы
        Text(
            text = "Тема приложения"
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row {

            // Светлая тема
            Button(
                onClick = {
                    onThemeChange(false)
                }
            ) {
                Text("День")
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            // Тёмная тема
            Button(
                onClick = {
                    onThemeChange(true)
                }
            ) {
                Text("Ночь")
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Отображение текущей темы
        Text(
            text = if (darkMode) {
                "Выбрана ночная тема"
            } else {
                "Выбрана дневная тема"
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text("Искусственная задержка запросов к базе данных")
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Switch(checked = databaseDelayEnabled, onCheckedChange = onDatabaseDelayChange)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (databaseDelayEnabled) "3 секунды" else "0 секунд")
        }
        Text(
            "Включите задержку, чтобы увидеть индикатор загрузки при операциях с базой.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { requestController.start(coroutineScope) },
            enabled = !requestState.isRunning
        ) {
            Text("Тестовый запрос к БД (10 секунд)")
        }

        if (requestState.isRunning) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Выполняется запрос...")
            }
        } else if (requestState.result != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Результат запроса: ${requestState.result}")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Возврат в меню
        OutlinedButton(
            onClick = onBack
        ) {
            Text("Назад")
        }
    }
}
