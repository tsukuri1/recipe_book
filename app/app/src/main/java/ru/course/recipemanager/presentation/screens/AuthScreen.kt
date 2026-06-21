package ru.course.recipemanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.course.recipemanager.ui.theme.Berry
import ru.course.recipemanager.ui.theme.Milk
import ru.course.recipemanager.ui.theme.Mint
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Panel
import ru.course.recipemanager.ui.theme.Sage

@Composable
fun AuthScreen(
    message: String?,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String) -> Unit,
    onMessageShown: () -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("Илья") }
    var email by remember { mutableStateOf("user@ladushki.app") }
    var password by remember { mutableStateOf("1234") }

    LaunchedEffect(message) {
        if (message != null) {
            delay(1800)
            onMessageShown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Milk)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Добро пожаловать", color = Berry, fontWeight = FontWeight.ExtraBold)
        Text("Ладушки", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Войдите, чтобы сохранять рецепты, покупки, избранное и домашние кулинарные заметки.",
            color = Muted
        )

        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    if (isRegisterMode) "Создать аккаунт" else "Вход в аккаунт",
                    style = MaterialTheme.typography.headlineMedium
                )
                if (isRegisterMode) {
                    AuthField("Имя", name, { name = it })
                }
                AuthField("Email", email, { email = it })
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Пароль") },
                    shape = RoundedCornerShape(18.dp),
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(
                    onClick = {
                        if (isRegisterMode) {
                            onRegister(name, email, password)
                        } else {
                            onLogin(email, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Berry)
                ) {
                    Text(if (isRegisterMode) "Зарегистрироваться" else "Войти", fontWeight = FontWeight.ExtraBold)
                }

                Button(
                    onClick = { isRegisterMode = !isRegisterMode },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Sage)
                ) {
                    Text(if (isRegisterMode) "Уже есть аккаунт" else "Создать аккаунт")
                }
            }
        }

        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Mint)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Админ-вход", fontWeight = FontWeight.Bold)
                    Text("email: admin@ladushki.app", color = Muted)
                }
                Text("роль ADMIN", color = Berry, fontWeight = FontWeight.ExtraBold)
            }
        }

        if (message != null) {
            Snackbar(containerColor = Sage) {
                Text(message)
            }
        }
    }
}

@Composable
private fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        shape = RoundedCornerShape(18.dp),
        singleLine = true
    )
}
