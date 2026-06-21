package ru.course.recipemanager.presentation.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.course.recipemanager.entity.UserProfile
import ru.course.recipemanager.ui.theme.Berry
import ru.course.recipemanager.ui.theme.BerryDark
import ru.course.recipemanager.ui.theme.Milk
import ru.course.recipemanager.ui.theme.Mint
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Panel

@Composable
fun ProfileScreen(
    profile: UserProfile,
    onOpenSettings: () -> Unit,
    onOpenAdmin: () -> Unit,
    onUpdateProfile: (String, String?) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }
    var supportVisible by remember { mutableStateOf(false) }
    var draftName by remember(profile.name) { mutableStateOf(profile.name) }
    var draftAvatarUri by remember(profile.avatarUri) { mutableStateOf(profile.avatarUri) }
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val savedAvatar = uri.toString()
            draftAvatarUri = savedAvatar
            onUpdateProfile(draftName, savedAvatar)
            editMode = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Milk)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Rounded.MoreVert, contentDescription = "Меню профиля", tint = BerryDark)
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Настройки") },
                        onClick = {
                            menuExpanded = false
                            onOpenSettings()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Связь с поддержкой") },
                        onClick = {
                            menuExpanded = false
                            supportVisible = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Выйти из аккаунта") },
                        onClick = {
                            menuExpanded = false
                            onLogout()
                        }
                    )
                }
            }
        }

        ProfileAvatar(
            profile = profile.copy(avatarUri = draftAvatarUri),
            onClick = { imagePicker.launch(arrayOf("image/*")) }
        )
        Text(profile.name, style = MaterialTheme.typography.headlineLarge)
        Text(
            "Личные рецепты, избранное и аккуратные покупки в одном месте.",
            color = Muted,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = { editMode = !editMode },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = BerryDark)
        ) {
            Text(if (editMode) "Свернуть редактирование" else "Изменить профиль", fontWeight = FontWeight.Bold)
        }

        if (editMode) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Panel)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = draftName,
                        onValueChange = { draftName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Имя") },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { imagePicker.launch(arrayOf("image/*")) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = BerryDark)
                        ) {
                            Text("Выбрать аватар")
                        }
                        Button(
                            onClick = {
                                onUpdateProfile(draftName, draftAvatarUri)
                                editMode = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Berry)
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard("${profile.recipesCount}", "рецептов", Modifier.weight(1f))
            StatCard("${profile.favoriteCount}", "избранных", Modifier.weight(1f))
            StatCard("${profile.offlineCount}", "сохранено", Modifier.weight(1f))
        }

        if (supportVisible) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Mint)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Поддержка", color = Berry, fontWeight = FontWeight.ExtraBold)
                    Text("Напишите нам: support@ladushki.app", color = Muted)
                }
            }
        }

        SettingRow("Роль пользователя", profile.role.label, if (profile.isAdmin) onOpenAdmin else onOpenSettings)
        if (profile.isAdmin) {
            SettingRow("Админ-панель", "Модерация", onOpenAdmin)
        }
    }
}

@Composable
private fun ProfileAvatar(
    profile: UserProfile,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(112.dp)
            .clickable { onClick() },
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Berry),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (!profile.avatarUri.isNullOrBlank()) {
                AsyncImage(
                    model = profile.avatarUri,
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = profile.name.take(1).uppercase(),
                    color = Panel,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = Berry, style = MaterialTheme.typography.headlineMedium)
            Text(label, color = Muted, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SettingRow(title: String, value: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(value, color = Berry, fontWeight = FontWeight.ExtraBold)
        }
    }
}
