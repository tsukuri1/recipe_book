package ru.course.recipemanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.course.recipemanager.entity.UserSettings
import ru.course.recipemanager.ui.theme.Berry
import ru.course.recipemanager.ui.theme.Milk
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Panel

@Composable
fun SettingsScreen(
    settings: UserSettings,
    onBack: () -> Unit,
    onSave: (UserSettings) -> Unit
) {
    var draft by remember(settings) { mutableStateOf(settings) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Milk)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        androidx.compose.foundation.layout.Spacer(Modifier.height(14.dp))
        Button(
            onClick = onBack,
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Berry)
        ) {
            Text("Назад")
        }
        Text("Настройки", color = Berry, fontWeight = FontWeight.ExtraBold)
        Text("Комфорт и уведомления", style = MaterialTheme.typography.headlineLarge)
        Text("Настройте напоминания, сохранение рецептов и удобный вид карточек.", color = Muted)

        SettingsToggle(
            title = "Сохранять мои изменения",
            subtitle = "Рецепты и покупки останутся в аккаунте",
            checked = draft.syncEnabled,
            onCheckedChange = { draft = draft.copy(syncEnabled = it) }
        )
        SettingsToggle(
            title = "Доступ без интернета",
            subtitle = "Открывать сохраненные рецепты даже без сети",
            checked = draft.offlineCacheEnabled,
            onCheckedChange = { draft = draft.copy(offlineCacheEnabled = it) }
        )
        SettingsToggle(
            title = "Напоминания",
            subtitle = "Показывать уведомления о списке покупок",
            checked = draft.notificationsEnabled,
            onCheckedChange = { draft = draft.copy(notificationsEnabled = it) }
        )
        SettingsToggle(
            title = "Компактный режим",
            subtitle = "Плотнее показывать карточки рецептов",
            checked = draft.compactModeEnabled,
            onCheckedChange = { draft = draft.copy(compactModeEnabled = it) }
        )

        Button(
            onClick = { onSave(draft) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Berry)
        ) {
            Text("Сохранить настройки", fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun SettingsToggle(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.ExtraBold)
                Text(subtitle, color = Muted)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
