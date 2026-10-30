package com.example.laba6.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.laba6.data.User

@Composable
fun ProfileScreen(user: User, adminCanPromote: Boolean, onPromote: () -> Unit, onBack: () -> Unit, loading: Boolean) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Профиль", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("ФИО: ${user.fullName}"); Text("Логин: ${user.login}"); Text("Дата рождения: ${user.birthDate}")
        Text("Пол: ${user.gender}"); Text("Роль: ${if (user.isAdmin) "Администратор" else "Пользователь"}")
        Spacer(Modifier.height(16.dp))
        if (adminCanPromote && !user.isAdmin) Button(onClick = onPromote, enabled = !loading) {
            if (loading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp) else Text("Сделать администратором")
        }
        OutlinedButton(onClick = onBack, enabled = !loading) { Text("Назад") }
    }
}
